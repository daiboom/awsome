package org.startlight.awsome.constants;

public class UserApiExamples {
  private UserApiExamples() {
    throw new UnsupportedOperationException("Utility class");
  }

  // User 예제
  public static final String USER_SINGLE = """
      {
        "id": 1,
        "name": "홍길동",
        "joinDate": "2024-01-01T00:00:00.000+00:00"
      }
      """;

  public static final String USER_LIST = """
      [
        {
          "id": 1,
          "name": "홍길동",
          "joinDate": "2024-01-01T00:00:00.000+00:00"
        },
        {
          "id": 2,
          "name": "김철수",
          "joinDate": "2024-01-02T00:00:00.000+00:00"
        }
      ]
      """;

  public static final String USER_WITH_LINKS = """
      {
        "id": 1,
        "name": "홍길동",
        "joinDate": "2024-01-01T00:00:00.000+00:00",
        "_links": {
          "all-users": {
            "href": "http://localhost:8088/users"
          },
          "self": {
            "href": "http://localhost:8088/users/1"
          }
        }
      }
      """;

  // 에러 응답 예제
  public static final String ERROR_BAD_REQUEST = """
      {
        "error": "Bad Request",
        "message": "잘못된 요청입니다."
      }
      """;

  public static final String ERROR_NOT_FOUND = """
      {
        "error": "Not Found",
        "message": "ID[1] not found"
      }
      """;

  public static final String ERROR_NOT_FOUND_LIST = """
      {
        "error": "Not Found",
        "message": "사용자 목록을 찾을 수 없습니다."
      }
      """;

  public static final String ERROR_INTERNAL_SERVER = """
      {
        "error": "Internal Server Error",
        "message": "서버 내부 오류가 발생했습니다."
      }
      """;

  public static final String ERROR_VALIDATION = """
      {
        "error": "Bad Request",
        "message": "이름은 2자 이상 입력해주세요."
      }
      """;
}

