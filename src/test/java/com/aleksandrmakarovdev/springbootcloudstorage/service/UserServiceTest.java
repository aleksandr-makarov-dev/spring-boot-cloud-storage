package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.entity.User;
import com.aleksandrmakarovdev.springbootcloudstorage.exception.UserExistsException;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserServiceTest {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:17.0");

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer(POSTGRES_IMAGE)
            .withDatabaseName("tests")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        usersRepository.deleteAll();
    }


    @Test
    public void register_whenValidCredentialsAndUsernameNotExist_shouldCreateUser() {
        String username = "testuser";
        String password = "123456A";

        userService.registerUser(new RegisterUserRequest(username, password, password));

        assertEquals(1, usersRepository.count(), "Number of users should be 1");

        Optional<User> user = usersRepository.findByUsername(username);

        assertTrue(user.isPresent(), "User should be found");
        assertEquals(username, user.get().getUsername(), "Username should be the same");
        assertNotEquals(password, user.get().getPasswordHash(), "Password should not be saved in raw format.");
    }

    @Test
    public void register_whenValidCredentialsAndUsernameExists_shouldThrowException() {
        String username = "testuser";
        String password = "123456A";

        userService.registerUser(new RegisterUserRequest(username, password, password));

        assertThrows(
                UserExistsException.class,
                () -> userService.registerUser(new RegisterUserRequest(username, password, password)),
                "Should throw UserExistsException if username exists"
        );

        assertEquals(1, usersRepository.count(), "Number of users should be 1");


    }
}