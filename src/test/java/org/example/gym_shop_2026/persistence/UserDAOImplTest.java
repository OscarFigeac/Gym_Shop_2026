package org.example.gym_shop_2026.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {
    @Mock
    private UserDAOImpl userDAO;

    @Test
    void loginWithNullUsernameOrNullPassword() {
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login(null, "test123");
        }
        );
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login("testUser123", null);
        }
        );
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login(null, null);
        }
        );
    }
    @Test
    void loginWithEmptyUsernameOrEmptyPassword() {
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login("", "test123");
        });
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login("testUser123", "");
        });
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login("", "");
        });
    }
    @Test
    void loginWithBlankUsernameOrBlankPassword() {
        assertThrows(NullPointerException.class, () -> {
           this.userDAO.login(" ", "test123");
        });
        assertThrows(NullPointerException.class, () -> {
           this.userDAO.login("NonBlankUsername", " ");
        });
        assertThrows(NullPointerException.class, () -> {
           this.userDAO.login(" ", "      ");
        });
    }
    @Test
    void loginWithCredentialsOfNonExistingUser() {
        assertThrows(NullPointerException.class, () -> {
            this.userDAO.login("testUser123", "test123");
        });
    }
}