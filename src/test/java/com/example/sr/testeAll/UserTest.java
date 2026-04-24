package com.example.sr.testeAll;

import com.example.sr.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserInitialization() {
        User user = new User();
        assertNotNull(user.getActivities());
        assertTrue(user.getActivities().isEmpty());
    }

    @Test
    void testUserDetailsAndInitialization() {

        User user = new User();
        user.setEmail("kayo@alves.com");


        assertNotNull(user.getActivities());
        assertTrue(user.getActivities().isEmpty());

        assertEquals("kayo@alves.com", user.getUsername());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
