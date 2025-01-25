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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        book.setIsAvailable("available");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        rentalService.rentBook(bookId, userId);

        assertEquals("rented", book.getIsAvailable());

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
        book.setIsAvailable("rented");

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setReturnDate(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(rentalRepository.findByBookAndUser(book, user)).thenReturn(Collections.singletonList(rental));

        rentalService.returnBook(bookId, userId);

        assertNotNull(rental.getReturnDate());
        assertEquals("available", book.getIsAvailable());

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

    @DisplayName("Find Rentals By User When Rentals Exist")
    @Test
    public void testFindRentalsByUserWhenRentalsExist() {

        Long userId = 1L;

        Rental rental1 = new Rental();
        Rental rental2 = new Rental();

        List<Rental> expectedRentals = Arrays.asList(rental1, rental2);

        when(rentalRepository.findByUser_IdAndHiddenFalse(userId)).thenReturn(expectedRentals);

        List<Rental> actualRentals = rentalService.findRentalsByUser(userId);

        assertNotNull(actualRentals);
        assertEquals(2, actualRentals.size());
        assertEquals(expectedRentals, actualRentals);

        verify(rentalRepository, times(1)).findByUser_IdAndHiddenFalse(userId);
    }

    @DisplayName("Find Rentals By User When No Rentals Exist")
    @Test
    public void testFindRentalsByUserWhenNoRentalsExist() {

        Long userId = 1L;

        when(rentalRepository.findByUser_IdAndHiddenFalse(userId)).thenReturn(Arrays.asList());

        List<Rental> acutalRentals = rentalService.findRentalsByUser(userId);

        assertNotNull(acutalRentals);
        assertTrue(acutalRentals.isEmpty());

        verify(rentalRepository, times(1)).findByUser_IdAndHiddenFalse(userId);
    }

    @DisplayName("Hide Rental Should Hide Rental When Rental Exists")
    @Test
    public void testHideRentalShouldHideRentalWhenRentalExists() {

        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setHidden(false);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        rentalService.hideRental(rentalId);

        assertTrue(rental.isHidden(), "Rental should be hidden");

        verify(rentalRepository).save(rental);
    }

    @DisplayName("Hide Rental Should Throw Exception When Rental Not Found")
    @Test
    public void testHideRentalShouldThrowExceptionWhenRentalNotFound() {

        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.hideRental(rentalId));

        verify(rentalRepository, never()).save(any());
    }

    @DisplayName("Find Rental By Id And User Should Return Rental When Rental Exists")
    @Test
    public void testFindRentalByIdAndUserShouldReturnRentalWhenRentalExists() {

        Long rentalId = 1L;
        Long userId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);

        when(rentalRepository.findByIdAndUserId(rentalId, userId)).thenReturn(Optional.of(rental));

        Optional<Rental> result = rentalService.findRentalByIdAndUser(rentalId, userId);

        assertTrue(result.isPresent(), "Rental should be found");
        assertEquals(rentalId, result.get().getId(), "The rental ID should match");

        verify(rentalRepository).findByIdAndUserId(rentalId, userId);
    }

    @DisplayName("Find Rental By Id And User Should Return Empty When Rental Does Not Exist")
    @Test
    public void testFindRentalByIdAndUserShouldReturnEmptyWhenRentalDoesNotExist() {

        Long rentalId = 1L;
        Long userId = 1L;

        when(rentalRepository.findByIdAndUserId(rentalId, userId)).thenReturn(Optional.empty());

        Optional<Rental> result = rentalService.findRentalByIdAndUser(rentalId, userId);

        assertFalse(result.isPresent(), "Rental should not be found");

        verify(rentalRepository).findByIdAndUserId(rentalId, userId);
    }
}