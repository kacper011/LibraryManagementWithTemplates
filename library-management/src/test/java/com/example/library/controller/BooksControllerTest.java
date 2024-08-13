package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BooksControllerTest {

    @Mock
    private BookService bookService;
    @Mock
    private Model model;
    @InjectMocks
    private BooksController booksController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getBooksAdminTest() {
        Book book1 = new Book(1L, "Book title 1", "Author 1");
        Book book2 = new Book(2L, "Book title 2", "Author 2");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        String viewName = booksController.getBooksAdmin(model);

        assertEquals("books_admin", viewName);
        verify(model).addAttribute("books", books);
    }


    @Test
    public void addBookTest() {
        String viewName = booksController.addBook(model);

        assertEquals("create_book_admin", viewName);

        verify(model).addAttribute(eq("book"), any(Book.class));
    }

}