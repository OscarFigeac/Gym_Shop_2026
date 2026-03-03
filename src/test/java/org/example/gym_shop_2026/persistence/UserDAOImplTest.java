package org.example.gym_shop_2026.persistence;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;

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