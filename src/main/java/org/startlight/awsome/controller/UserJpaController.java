package org.startlight.awsome.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.startlight.awsome.bean.User;
import org.startlight.awsome.dto.PageResponse;
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
    public ResponseEntity<PageResponse<User>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        // 필터링 조건에 따라 다른 메서드 호출
        if (name != null && !name.isEmpty()) {
            if (startDate != null && endDate != null) {
                // 이름과 날짜 범위 모두 필터링
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = sdf.parse(startDate);
                    Date end = sdf.parse(endDate);
                    userPage = userRepository
                            .findByNameContainingAndJoinDateBetween(name, start,
                                    end, pageable);
                } catch (ParseException e) {
                    userPage = userRepository.findByNameContaining(name,
                            pageable);
                }
            } else {
                // 이름만 필터링
                userPage = userRepository.findByNameContaining(name, pageable);
            }
        } else if (startDate != null && endDate != null) {
            // 날짜 범위만 필터링
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                userPage = userRepository.findByJoinDateBetween(start, end,
                        pageable);
            } catch (ParseException e) {
                userPage = userRepository.findAll(pageable);
            }
        } else {
            // 필터링 없음
            userPage = userRepository.findAll(pageable);
        }

        int totalCount = (int) userPage.getTotalElements();
        int currentPage = userPage.getNumber();
        int offset = currentPage * size;
        List<User> data = userPage.getContent();
        boolean hasPrev = userPage.hasPrevious();
        boolean hasNext = userPage.hasNext();

        // 다음 페이지가 있으면 cursor 생성 (필터 파라미터 포함)
        String cursor = null;
        if (hasNext) {
            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                    .fromCurrentRequest();
            // 기존 쿼리 파라미터를 교체하여 중복 방지
            builder.replaceQueryParam("page", currentPage + 1);
            builder.replaceQueryParam("size", size);

            if (name != null && !name.isEmpty()) {
                builder.replaceQueryParam("name", name);
            } else {
                builder.replaceQueryParam("name"); // 파라미터 제거
            }
            if (startDate != null) {
                builder.replaceQueryParam("startDate", startDate);
            } else {
                builder.replaceQueryParam("startDate"); // 파라미터 제거
            }
            if (endDate != null) {
                builder.replaceQueryParam("endDate", endDate);
            } else {
                builder.replaceQueryParam("endDate"); // 파라미터 제거
            }

            cursor = builder.build().toUriString();
        }

        PageResponse<User> response = new PageResponse<>(totalCount,
                currentPage, offset, data, cursor, hasPrev, hasNext);

        return ResponseEntity.ok(response);
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
        entityModel.add(linkTo(methodOn(UserJpaController.class).findAll(0, 10,
                null, null, null)).withRel("all-users"));
        entityModel.add(
                linkTo(methodOn(UserJpaController.class).findOne(user.getId()))
                        .withSelfRel());
        return entityModel;
    }
}
