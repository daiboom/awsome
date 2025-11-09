package org.startlight.awsome.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.startlight.awsome.bean.User;
import org.startlight.awsome.exception.UserNotFoundException;
import org.startlight.awsome.repository.UserRepository;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    private final UserRepository userRepository;

    public UserJpaController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> findOne(@PathVariable int id) {
        User user = requireUser(id);
        EntityModel<User> entityModel = toEntityModel(user);
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        // 저장 후 다시 조회하여 id가 확실히 포함되도록 함
        User userWithId = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("ID[%s] not found", savedUser.getId())));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userWithId.getId()).toUri();

        return ResponseEntity.created(location).body(userWithId);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        requireUser(id);
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable int id,
            @RequestBody User user) {
        requireUser(id);
        user.setId(id);
        User updatedUser = userRepository.save(user);
        EntityModel<User> entityModel = toEntityModel(updatedUser);
        return ResponseEntity.ok(entityModel);
    }

    private @NonNull User requireUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("ID[%s] not found", id)));
    }

    private EntityModel<User> toEntityModel(User user) {
        EntityModel<User> entityModel = EntityModel.of(user);
        entityModel.add(linkTo(methodOn(UserJpaController.class).findAll())
                .withRel("all-users"));
        entityModel.add(
                linkTo(methodOn(UserJpaController.class).findOne(user.getId()))
                        .withSelfRel());
        return entityModel;
    }
}
