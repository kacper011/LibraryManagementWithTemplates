package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentalRepository;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private RentalRepository rentalRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @DisplayName("Save User Success")
    @Test
    public void testSaveUserSuccess() {

        User user = new User();
        user.setPassword("testPassword");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        userServiceImpl.saveUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Set.of(userRole), user.getRoles());

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Save User Role Not Found")
    @Test
    public void testSaveUserRoleNotFound() {

        User user = new User();

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.saveUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Update Email Success")
    @Test
    public void testUpdateEmailSuccess() {

        Long userId = 1L;
        String newEmail = "newemail@gmail.com";
        User user = new User();
        user.setId(userId);
        user.setEmail("oldemail@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.updateEmail(userId, newEmail);

        assertEquals(newEmail, user.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Update Email User Not Found")
    @Test
    public void testUpdateEmailUserNotFound() {

        Long userId = 1L;
        String newEmail = "newemail@gmail.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updateEmail(userId, newEmail);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Update Username Success")
    @Test
    public void testUpdateUsernameSuccess() {

        Long userId = 1L;
        String newUsername = "newUsername";
        User user = new User();
        user.setId(userId);
        user.setName("oldUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.updateUsername(userId, newUsername);

        assertEquals(newUsername, user.getName());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Update Username User Not Found")
    @Test
    public void testUpdateUsernameUserNotFound() {

        Long userId = 1L;
        String newUsername = "newUsername";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updateUsername(userId, newUsername);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Update Password Should Update Password When User Exists")
    @Test
    public void testUpdatePasswordShouldUpdatePasswordWhenUserExists() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userServiceImpl.updatePassword(userId, newPassword);

        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode(newPassword);
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @DisplayName("Update Password Should Throw Exception When User Does Not Exist")
    @Test
    public void testUpdatePasswordShouldThrowExceptionWhenUserDoesNotExist() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updatePassword(1L, "newPassword");
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @DisplayName("Update Profile Should Update Profile When User Exists And Data Is Different")
    @Test
    public void testUpdateProfileShouldUpdateProfileWhenUserExistsAndDataIsDifferent() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("oldUsername");
        user.setEmail("oldEmail@gmail.com");
        user.setPassword("oldPassword");

        String newUsername = "newUsername";
        String newEmail = "newEmail@gmail.com";
        String newPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userServiceImpl.updateProfil(userId, newUsername, newEmail, newPassword);

        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode(newPassword);
        assertEquals(newUsername, user.getName());
        assertEquals(newEmail, user.getEmail());
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @DisplayName("Update Profile Should Not Update Profile When Data Is Same")
    @Test
    public void testUpdateProfileShouldNotUpdateProfileWhenDataIsSame() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("oldUsername");
        user.setEmail("oldEmail@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);

        userServiceImpl.updateProfil(userId, "oldUsername", "oldEmail@gmail.com", "oldPassword");

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
        verify(passwordEncoder).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @DisplayName("Update Profile Should Throw Exception When User Does Not Exist")
    @Test
    public void testUpdateProfileShouldThrowExceptionWhenUserDoesNotExist() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updateProfil(userId, "newUsername", "newEmail@gmail.com", "newPassword");
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @DisplayName("Delete User By Id User Exists")
    @Test
    public void testDeleteUserByIdUserExists() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Book book1 = new Book();
        book1.setIsAvailable("rented");

        Book book2 = new Book();
        book2.setIsAvailable("rented");

        Rental rental1 = new Rental();
        rental1.setBook(book1);

        Rental rental2 = new Rental();
        rental2.setBook(book2);

        user.setRentals(Arrays.asList(rental1, rental2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUserById(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(bookRepository, times(1)).save(book1);
        verify(bookRepository, times(1)).save(book2);
        verify(rentalRepository, times(1)).save(rental1);
        verify(rentalRepository, times(1)).save(rental2);
        verify(userRepository, times(1)).delete(user);

        assertEquals("available", book1.getIsAvailable());
        assertEquals("available", book2.getIsAvailable());
    }

    @DisplayName("Delete User By Id User Not Found")
    @Test
    public void testDeleteUserByIdUserNotFound() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userServiceImpl.deleteUserById(userId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

}