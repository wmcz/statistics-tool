package cz.cvut.fit.wikimetric.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {
    @Test
    void getID() {
        User user = new User(1L);
        assert user.getID() == 1;
    }
}