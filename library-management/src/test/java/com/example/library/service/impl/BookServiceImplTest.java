package com.example.library.service.impl;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    public void testCreateBook() {

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsAvailable("dostępna");

        bookService.createBook(book);

        verify(bookRepository, times(1)).save(book);
    }

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

    @Test
    public void testGetBookByIdNotFound() {

        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(bookId);
        });

        verify(bookRepository, times(1)).findById(bookId);
    }

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
}