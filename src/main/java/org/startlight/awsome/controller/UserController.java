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
import org.startlight.awsome.constants.UserApiExamples;
import org.startlight.awsome.dao.UserDaoService;
import org.startlight.awsome.exception.UserNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UserController {
  private UserDaoService userDaoService;

  public UserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = UserApiExamples.USER_LIST))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_BAD_REQUEST))),
      @ApiResponse(responseCode = "404", description = "사용자 목록 조회 실패", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_NOT_FOUND_LIST))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_INTERNAL_SERVER))) })
  @GetMapping("/users")
  public ResponseEntity<List<User>> findAll() {
    List<User> users = userDaoService.findAll();
    if (users.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(userDaoService.findAll());
  }

  @Operation(summary = "사용자 조회", description = "ID로 특정 사용자를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = UserApiExamples.USER_WITH_LINKS))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_NOT_FOUND))) })
  @GetMapping("/users/{id}")
  public EntityModel<User> findOne(@PathVariable int id) {
    User user = requireUser(id);

    EntityModel<User> entityModel = EntityModel.of(user);

    entityModel.add(
        linkTo(methodOn(UserController.class).findAll()).withRel("all-users"));
    entityModel
        .add(linkTo(methodOn(UserController.class).findOne(id)).withSelfRel());

    return entityModel;
  }

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "사용자 생성 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.USER_SINGLE))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_VALIDATION))) })
  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userDaoService.save(user);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(savedUser.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "사용자 삭제", description = "ID로 특정 사용자를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "사용자 삭제 성공", content = @Content),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_NOT_FOUND))) })
  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable int id) {
    User user = requireUser(id);
    userDaoService.delete(user);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자 수정", description = "ID로 특정 사용자의 정보를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "사용자 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = UserApiExamples.USER_SINGLE))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_NOT_FOUND))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = UserApiExamples.ERROR_VALIDATION))) })
  @PatchMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable int id,
      @RequestBody User user) {
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
