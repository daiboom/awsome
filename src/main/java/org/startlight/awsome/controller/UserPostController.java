package org.startlight.awsome.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.startlight.awsome.bean.Post;
import org.startlight.awsome.exception.UserNotFoundException;
import org.startlight.awsome.repository.PostRepository;
import org.startlight.awsome.repository.UserRepository;

@RestController
public class UserPostController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserPostController(UserRepository userRepository,
            PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/user/{id}/posts")
    public ResponseEntity<List<Post>> findUserPosts(@PathVariable int id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("ID[%s] not found", id)));
        List<Post> posts = postRepository.findByUserId(id);
        return ResponseEntity.ok(posts);
    }
}
