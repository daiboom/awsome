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
import org.startlight.awsome.bean.User;
import org.startlight.awsome.dao.UserDaoService;
import org.startlight.awsome.exception.UserNotFoundException;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
  private UserDaoService userDaoService;

  public AdminUserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  // /admin/users/{id}
  @GetMapping("/users/{id}")
  public MappingJacksonValue findOne(@PathVariable int id) {
    User user = userDaoService.findOne(id);

    AdminUser adminUser = new AdminUser();

    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    } else {
      BeanUtils.copyProperties(user, adminUser);
    }

    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(adminUser);
    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }

  // /admin/users
  @GetMapping("/users")
  public MappingJacksonValue findAll() {
    List<User> users = userDaoService.findAll();

    List<AdminUser> adminUsers = new ArrayList<>();

    for (User user : users) {
      AdminUser adminUser = new AdminUser();
      BeanUtils.copyProperties(user, adminUser);
      adminUsers.add(adminUser);
    }

    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(adminUsers);
    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }

}
