package org.startlight.awsome.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<User> findOne(@PathVariable int id) {
    User user = userDaoService.findOne(id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(user);
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
    User user = userDaoService.findOne(id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    userDaoService.delete(user);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
    User updatedUser = userDaoService.update(id, user);
    if (updatedUser == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedUser);
  }

}
