package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentalRepository;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Rent Book Success")
    @Test
    public void testRentBookSuccess() {

        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);
        book.setIsAvailable("dostępna");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        rentalService.rentBook(bookId, userId);

        assertEquals("wypożyczona", book.getIsAvailable());

        verify(bookRepository, times(1)).save(book);

        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @DisplayName("Rent Book User Not Found")
    @Test
    public void testRentBooksUserNotFound() {

        Long userId = 1L;
        Long bookId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            rentalService.rentBook(bookId, userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @DisplayName("Rent Book Already Rented")
    @Test
    public void testRentBookAlreadyRented() {

        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);
        book.setIsAvailable("wypożyczona");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            rentalService.rentBook(bookId, userId);
        });

        assertEquals("Book is already rented", exception.getMessage());
    }

    @DisplayName("Return Book Successful Return")
    @Test
    public void testReturnBookSuccessfulReturn() {

        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);
        book.setIsAvailable("wypożyczona");

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setReturnDate(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(rentalRepository.findByBookAndUser(book, user)).thenReturn(Collections.singletonList(rental));

        rentalService.returnBook(bookId, userId);

        assertNotNull(rental.getReturnDate());
        assertEquals("dostępna", book.getIsAvailable());

        verify(rentalRepository, times(1)).save(rental);
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Return Book User Not Found")
    @Test
    public void testReturnBookUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            rentalService.returnBook(1L, 1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @DisplayName("Return Book No Active Rental Found")
    @Test
    public void testReturnBookNoActiveRentalFound() {

        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(rentalRepository.findByBookAndUser(book, user)).thenReturn(Collections.EMPTY_LIST);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            rentalService.returnBook(bookId, userId);
        });

        assertEquals("No active rental record found for this book and user", exception.getMessage());
    }
}