package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.User;
import com.pdds.domain.enums.Role;
import com.pdds.dto.ChangePasswordDTO;
import com.pdds.dto.UpdateUserDTO;
import com.pdds.dto.UserDTO;
import com.pdds.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach(){

        testConfig.cleanDatabase();

    }

    @Test
    public void getAllUsersTest() throws Exception{

        List<User> users = userService.getAll();

        Assertions.assertNotNull(users);

    }

    @Test
    public void findUserByIdTest() throws Exception{

        User user = (User) userRepository.findByEmail("john.doe@example.com");
        Optional<User> opt = userService.findById(user.getId());

        Assertions.assertTrue(opt.isPresent());

        User retrievedUser = opt.get();

        Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
        Assertions.assertEquals(user.getFullName(), retrievedUser.getFullName());
        Assertions.assertEquals(user.getBirthday(), retrievedUser.getBirthday());
        Assertions.assertEquals(user.getRole(), retrievedUser.getRole());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches("securepassword123", retrievedUser.getPassword()));

    }

    @Test
    public void updateUserTest() throws Exception{

        User user = (User) userRepository.findByEmail("clark.kent@example.com");
        UpdateUserDTO newData = new UpdateUserDTO(
                "leo.mills@example.com",
                "Leo Mills",
                "22/11/2001",
                Role.ADMIN
        );

        boolean update = userService.update(user.getId(), newData);

        User updatedUser = userService.findById(user.getId()).get();

        Assertions.assertTrue(update);
        Assertions.assertEquals(newData.email(), updatedUser.getEmail());
        Assertions.assertEquals(newData.fullName(), updatedUser.getFullName());
        Assertions.assertEquals(newData.role(), updatedUser.getRole());

    }

    @Test
    public void deleteUserTest() throws Exception{

        User user = (User) userRepository.findByEmail("clark.kent@example.com");

        boolean delete = userService.delete(user.getId());

        Assertions.assertTrue(delete);
        Assertions.assertFalse(userService.delete(user.getId()));

    }

    @Test
    public void changeUserPasswordTest() throws Exception{

        User user = (User) userRepository.findByEmail("romeo.kling@example.com");
        ChangePasswordDTO cp = new ChangePasswordDTO("passwordallaround", "wordpass");

        boolean changePw = userService.changePassword(user.getId(), cp);

        Assertions.assertTrue(changePw);
        Assertions.assertFalse(userService.changePassword(user.getId(), cp));

    }

    @Test
    public void findUserByEmail() throws Exception{

        UserDTO user = new UserDTO(
                "rick.richards@example.com",
                "thesafestpassword",
                "Rick Richards",
                "14/03/1999",
                Role.USER
        );

        userService.create(user);

        User retrievedUser = (User) userService.findByEmail(user.email());

        Assertions.assertEquals(user.email(), retrievedUser.getEmail());
        Assertions.assertEquals(user.fullName(), retrievedUser.getFullName());
        Assertions.assertEquals(user.role(), retrievedUser.getRole());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches(user.password(), retrievedUser.getPassword()));

    }

    @Test
    public void createUserTest() throws Exception{

        UserDTO userDTO = new UserDTO(
                "jimmy.quill@example.com",
                "apasswordtoremember",
                "Jimmy Quill",
                "02/07/1998",
                Role.ADMIN
        );

        boolean registration = userService.create(userDTO);

        Assertions.assertTrue(registration);
        Assertions.assertFalse(userService.create(userDTO));

    }

}
