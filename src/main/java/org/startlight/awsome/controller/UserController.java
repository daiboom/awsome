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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.startlight.awsome.bean.User;
import org.startlight.awsome.dao.UserDaoService;
import org.startlight.awsome.exception.UserNotFoundException;

@RestController
public class UserController {
  private UserDaoService userDaoService;

  public UserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> findAll() {
    List<User> users = userDaoService.findAll();
    if (users.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(userDaoService.findAll());
  }

  @GetMapping("/users/{id}")
  public EntityModel<User> findOne(@PathVariable int id) {
    User user = requireUser(id);

    EntityModel<User> entityModel = EntityModel.of(user);

    entityModel.add(linkTo(methodOn(UserController.class).findAll()).withRel("all-users"));
    entityModel.add(linkTo(methodOn(UserController.class).findOne(id)).withSelfRel());

    return entityModel;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userDaoService.save(user);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedUser.getId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable int id) {
    User user = requireUser(id);
    userDaoService.delete(user);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
    requireUser(id);
    User updatedUser = userDaoService.update(id, user);
    return ResponseEntity.ok(updatedUser);
  }

  private @NonNull User requireUser(int id) {
    User user = userDaoService.findOne(id);
    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    }
    return user;
  }

}
