package com.example.sr.testeAll;

import com.example.sr.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserInitialization() {
        User user = new User();
        assertNotNull(user.getActivities());
        assertTrue(user.getActivities().isEmpty());
    }
}
