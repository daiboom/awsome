package org.startlight.awsome.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User 도메인 객체")
@Entity
@Table(name = "users")
// @JsonIgnoreProperties(value = { "password", "ssn" }) 상위에서 처리 가능
public class User {

  @Schema(title = "ID", description = "ID", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Schema(title = "이름", description = "이름", example = "홍길동")
  @Size(min = 2, message = "이름은 2자 이상 입력해주세요.")
  private String name;

  @Schema(title = "등록일", description = "등록일", example = "2024-01-01T00:00:00.000+00:00")
  @Past(message = "등록일은 미래 날짜일 수 없습니다.")
  private Date joinDate;

  @Schema(title = "비밀번호", description = "비밀번호")
  @JsonIgnore
  private String password;

  @Schema(title = "주민번호", description = "주민번호")
  @JsonIgnore
  private String ssn;
}