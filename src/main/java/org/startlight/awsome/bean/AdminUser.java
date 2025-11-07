package org.startlight.awsome.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFilter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("UserInfo")
@Schema(description = "관리자용 User 정보 (V1)", example = """
    {
      "id": 1,
      "name": "홍길동",
      "joinDate": "2024-01-01T00:00:00.000+00:00",
      "ssn": "123456-1234567"
    }
    """)
public class AdminUser {
  @Schema(example = "1")
  private Integer id;

  @Schema(example = "홍길동")
  @Size(min = 2, message = "이름은 2자 이상 입력해주세요.")
  private String name;

  @Schema(example = "2024-01-01T00:00:00.000+00:00")
  @Past(message = "등록일은 미래 날짜일 수 없습니다.")
  private Date joinDate;

  private String password;

  @Schema(example = "123456-1234567")
  private String ssn;
}
