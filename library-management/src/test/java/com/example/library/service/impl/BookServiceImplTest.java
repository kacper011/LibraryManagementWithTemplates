package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @DisplayName("Get All Books")
    @Test
    public void testGetAllBooks() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("Create Book")
    @Test
    public void testCreateBook() {

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsAvailable("dostępna");

        bookService.createBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Get Book By Id")
    @Test
    public void testGetBookById() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsAvailable("dostępna");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(bookId);

        assertNotNull(result);

        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals("dostępna", result.getIsAvailable());

        verify(bookRepository, times(1)).findById(bookId);
    }

    @DisplayName("Get Book By Id Not Found")
    @Test
    public void testGetBookByIdNotFound() {

        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(bookId);
        });

        verify(bookRepository, times(1)).findById(bookId);
    }

    @DisplayName("Update Book")
    @Test
    public void testUpdateBook() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Author");
        book.setAuthor("Test Title");
        book.setIsAvailable("dostępna");

        bookService.updateBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Delete Book")
    @Test
    public void testDeleteBook() {

        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Test Author");
        book.setTitle("Test Title");
        book.setIsAvailable("dostępna");

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @DisplayName("Rent Book Success")
    @Test
    public void testRentBookSuccess() {

        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Test Author");
        book.setTitle("Test Title");
        book.setIsAvailable("dostępna");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.rentBook(bookId);

        assertEquals("wypożyczona", book.getIsAvailable());
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Rent Book Throws Resource Not Found Exception")
    @Test
    public void testRentBooksThrowsResourceNotFound() {

        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.rentBook(bookId));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @DisplayName("Rent Book Throws Illegal State Exception")
    @Test
    public void testRentBookThrowsIllegalStateException() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Test Author");
        book.setTitle("Test Title");
        book.setIsAvailable("wypożyczona");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class, () -> bookService.rentBook(bookId));

        verify(bookRepository, never()).save(book);
    }
}