package org.startlight.awsome.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.startlight.awsome.bean.Post;
import org.startlight.awsome.bean.User;
import org.startlight.awsome.dto.PostRequest;
import org.startlight.awsome.exception.PostNotFoundException;
import org.startlight.awsome.exception.UserNotFoundException;
import org.startlight.awsome.repository.PostRepository;
import org.startlight.awsome.repository.UserRepository;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Post>> findAll() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> findOne(@PathVariable int id) {
        Post post = requirePost(id);
        EntityModel<Post> entityModel = toEntityModel(post);
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Post>> createPost(@RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setDescription(postRequest.getDescription());

        // user_id가 있는 경우 User 존재 여부 확인
        if (postRequest.getUserId() != null) {
            User user = userRepository.findById(postRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format("User ID[%s] not found", postRequest.getUserId())));
            post.setUser(user);
        }

        Post savedPost = postRepository.save(post);
        
        // 저장 후 다시 조회하여 id가 확실히 포함되도록 함
        Post postWithId = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new PostNotFoundException(
                        String.format("Post ID[%s] not found", savedPost.getId())));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(postWithId.getId()).toUri();

        EntityModel<Post> entityModel = toEntityModel(postWithId);
        return ResponseEntity.created(location).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> updatePost(@PathVariable int id,
            @RequestBody PostRequest postRequest) {
        Post post = requirePost(id);
        post.setId(id);
        post.setDescription(postRequest.getDescription());
        
        // user_id가 있는 경우 User 존재 여부 확인
        if (postRequest.getUserId() != null) {
            User user = userRepository.findById(postRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format("User ID[%s] not found", postRequest.getUserId())));
            post.setUser(user);
        }

        Post updatedPost = postRepository.save(post);
        EntityModel<Post> entityModel = toEntityModel(updatedPost);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        requirePost(id);
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Post requirePost(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(
                        String.format("Post ID[%s] not found", id)));
    }

    private EntityModel<Post> toEntityModel(Post post) {
        EntityModel<Post> entityModel = EntityModel.of(post);
        entityModel.add(linkTo(methodOn(PostController.class).findAll()).withRel("all-posts"));
        entityModel.add(
                linkTo(methodOn(PostController.class).findOne(post.getId())).withSelfRel());
        if (post.getUser() != null && post.getUser().getId() != null) {
            entityModel.add(
                    linkTo(methodOn(UserPostController.class).findUserPosts(post.getUser().getId()))
                            .withRel("user-posts"));
        }
        return entityModel;
    }
}

