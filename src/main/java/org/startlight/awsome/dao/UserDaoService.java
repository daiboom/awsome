package org.startlight.awsome.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.startlight.awsome.bean.User;

// DAO + Service
@Component
public class UserDaoService {
  private static List<User> users = new ArrayList<>();

  private static int usersCount = 3;

  static {
    users.add(new User(1, "User1", new Date(), "pass1", "901010-1111111"));
    users.add(new User(2, "User2", new Date(), "pass2", "901010-1111112"));
    users.add(new User(3, "User3", new Date(), "pass3", "901010-1111113"));
  }

  public List<User> findAll() {
    return users;
  }

  public User save(User user) {
    if (user.getId() == null) {
      user.setId(usersCount++);
    }

    if (user.getJoinDate() == null) {
      user.setJoinDate(new Date());
    }

    users.add(user);
    return user;
  }

  public User findOne(int id) {
    for (User user : users) {
      if (user.getId() == id) {
        return user;
      }
    }

    return null;
  }

  public void delete(User user) {
    users.remove(user);
  }

  public User update(int id, User user) {
    for (User u : users) {
      if (u.getId() == id) {
        u.setName(user.getName());
        return u;
      }
    }
    return null;
  }
}
