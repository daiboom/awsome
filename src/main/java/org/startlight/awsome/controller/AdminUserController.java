package org.startlight.awsome.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.startlight.awsome.bean.AdminUser;
import org.startlight.awsome.bean.AdminUserV2;
import org.startlight.awsome.bean.User;
import org.startlight.awsome.constants.AdminApiExamples;
import org.startlight.awsome.dao.UserDaoService;
import org.startlight.awsome.exception.UserNotFoundException;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService userDaoService;

    public AdminUserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // /admin/users/{id}
    // @GetMapping("/v1/users/{id}")
    // @GetMapping(value = "/users/{id}", params = "version=1")
    // @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
    @Operation(summary = "관리자용 사용자 조회 (V1)", description = "ID로 특정 사용자를 조회합니다. 주민번호(ssn) 정보를 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/vnd.company.appv1+json", schema = @Schema(implementation = AdminUser.class), examples = @ExampleObject(value = AdminApiExamples.ADMIN_USER_SINGLE))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = AdminApiExamples.ERROR_NOT_FOUND))) })
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue findOne(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        AdminUser adminUser = new AdminUser();

        if (user == null) {
            throw new UserNotFoundException(
                    String.format("ID[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user, adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
                adminUser);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    // /admin/users
    // @GetMapping("/v1/users")
    // @GetMapping(value = "/v1/users", params = "version=1")
    // @GetMapping(value = "/users", headers = "X-API-VERSION=1")
    @Operation(summary = "관리자용 사용자 목록 조회 (V1)", description = "모든 사용자 목록을 조회합니다. 주민번호(ssn) 정보를 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공", content = @Content(mediaType = "application/vnd.company.appv1+json", schema = @Schema(implementation = AdminUser.class), examples = @ExampleObject(value = AdminApiExamples.ADMIN_USER_LIST))),
            @ApiResponse(responseCode = "404", description = "사용자 목록을 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = AdminApiExamples.ERROR_NOT_FOUND_LIST))) })
    @GetMapping(value = "/users", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue findAll() {
        List<User> users = userDaoService.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();

        for (User user : users) {
            AdminUser adminUser = new AdminUser();
            BeanUtils.copyProperties(user, adminUser);
            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
                adminUsers);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    // /admin/v2/users/{id}
    // @GetMapping("/v2/users/{id}")
    // @GetMapping(value = "/users/{id}", params = "version=2")
    // @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @Operation(summary = "관리자용 사용자 조회 (V2)", description = "ID로 특정 사용자를 조회합니다. 등급(grade) 정보를 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/vnd.company.appv2+json", schema = @Schema(implementation = AdminUserV2.class), examples = @ExampleObject(value = AdminApiExamples.ADMIN_USER_V2_SINGLE))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = AdminApiExamples.ERROR_NOT_FOUND))) })
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue findOneV2(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        AdminUserV2 adminUser = new AdminUserV2();

        if (user == null) {
            throw new UserNotFoundException(
                    String.format("ID[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user, adminUser);
            adminUser.setGrade("VIP");
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("UserInfoV2", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
                adminUser);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    // /admin/v2/users
    // @GetMapping("/v2/users")
    // @GetMapping(value = "/users", params = "version=2")
    // @GetMapping(value = "/users", headers = "X-API-VERSION=2")
    @Operation(summary = "관리자용 사용자 목록 조회 (V2)", description = "모든 사용자 목록을 조회합니다. 등급(grade) 정보를 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공", content = @Content(mediaType = "application/vnd.company.appv2+json", schema = @Schema(implementation = AdminUserV2.class), examples = @ExampleObject(value = AdminApiExamples.ADMIN_USER_V2_LIST))),
            @ApiResponse(responseCode = "404", description = "사용자 목록을 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = AdminApiExamples.ERROR_NOT_FOUND_LIST))) })
    @GetMapping(value = "/users", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue findAllV2() {
        List<User> users = userDaoService.findAll();
        List<AdminUserV2> adminUsers = new ArrayList<>();

        for (User user : users) {
            AdminUserV2 adminUser = new AdminUserV2();
            BeanUtils.copyProperties(user, adminUser);
            adminUser.setGrade("VIP");
            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("UserInfoV2", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
                adminUsers);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
