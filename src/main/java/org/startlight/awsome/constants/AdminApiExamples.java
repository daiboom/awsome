package org.startlight.awsome.constants;

public class AdminApiExamples {
    private AdminApiExamples() {
        throw new UnsupportedOperationException("Utility class");
    }

    // AdminUser 예제 (V1 - ssn 포함)
    public static final String ADMIN_USER_SINGLE = """
            {
              "id": 1,
              "name": "홍길동",
              "joinDate": "2024-01-01T00:00:00.000+00:00",
              "ssn": "123456-1234567"
            }
            """;

    public static final String ADMIN_USER_LIST = """
            [
              {
                "id": 1,
                "name": "홍길동",
                "joinDate": "2024-01-01T00:00:00.000+00:00",
                "ssn": "123456-1234567"
              },
              {
                "id": 2,
                "name": "김철수",
                "joinDate": "2024-01-02T00:00:00.000+00:00",
                "ssn": "234567-2345678"
              }
            ]
            """;

    // AdminUserV2 예제 (V2 - grade 포함)
    public static final String ADMIN_USER_V2_SINGLE = """
            {
              "id": 1,
              "name": "홍길동",
              "joinDate": "2024-01-01T00:00:00.000+00:00",
              "grade": "VIP"
            }
            """;

    public static final String ADMIN_USER_V2_LIST = """
            [
              {
                "id": 1,
                "name": "홍길동",
                "joinDate": "2024-01-01T00:00:00.000+00:00",
                "grade": "VIP"
              },
              {
                "id": 2,
                "name": "김철수",
                "joinDate": "2024-01-02T00:00:00.000+00:00",
                "grade": "VIP"
              }
            ]
            """;

    // 에러 응답 예제
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
}
