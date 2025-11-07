package com.sparta.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // H2 DB 적용
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
        // 이제 DB 없이 contextLoads 통과
    }

}
