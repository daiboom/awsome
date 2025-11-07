package org.startlight.awsome.bean;

import java.util.Date;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
  private Integer id;

  @Size(min = 2, message = "Name must be at least 2 characters long")
  private String name;

  @Past(message = "Join date must be in the past")
  private Date joinDate;
}
