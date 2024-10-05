package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;
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

    @DisplayName("Get Books Admin")
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

    @DisplayName("Add Book")
    @Test
    public void addBookTest() {
        String viewName = booksController.addBook(model);

        assertEquals("create_book_admin", viewName);

        verify(model).addAttribute(eq("book"), any(Book.class));
    }

    @DisplayName("Should Return Create Book View When Validation Fails")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldReturnCreateBookViewWhenValidationFailsTest() throws Exception {

        Book invalidBook = new Book();
        invalidBook.setTitle("");

        mockMvc.perform(post("/books")
                .flashAttr("book", invalidBook)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("create_book_admin"));

        verify(bookService, never()).createBook(any(Book.class));
    }

}