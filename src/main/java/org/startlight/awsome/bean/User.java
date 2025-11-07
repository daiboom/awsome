package org.startlight.awsome.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// @JsonIgnoreProperties(value = { "password", "ssn" }) 상위에서 처리 가능
public class User {
  private Integer id;

  @Size(min = 2, message = "이름은 2자 이상 입력해주세요.")
  private String name;

  @Past(message = "등록일은 미래 날짜일 수 없습니다.")
  private Date joinDate;

  @JsonIgnore
  private String password;

  @JsonIgnore
  private String ssn;
}