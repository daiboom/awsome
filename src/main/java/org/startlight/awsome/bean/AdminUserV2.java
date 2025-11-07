package org.startlight.awsome.bean;

import com.fasterxml.jackson.annotation.JsonFilter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("UserInfoV2")
@Schema(description = "관리자용 User 정보 (V2)", example = """
    {
      "id": 1,
      "name": "홍길동",
      "joinDate": "2024-01-01T00:00:00.000+00:00",
      "grade": "VIP"
    }
    """)
public class AdminUserV2 extends AdminUser {
    @Schema(example = "VIP")
    private String grade;
}
