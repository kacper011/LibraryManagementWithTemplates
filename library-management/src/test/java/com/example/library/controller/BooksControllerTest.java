package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.BookService;
import com.example.library.service.RentalService;
import com.example.library.service.impl.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private UserRepository userRepository;
    @Mock
    private BookService bookService;
    @Mock
    private RentalServiceImpl rentalServiceImpl;
    @Mock
    private RentalService rentalService;
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

    // USER

    @DisplayName("Should Throw ResourceNotFoundException When User Not Found")
    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    public void testHideRentalUserNotFound() throws Exception {
        when(userRepository.findByName("testUser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/my_books/{rentalId}/hide", 1L)
                        .principal(() -> "testUser")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound());

        verify(rentalService, never()).hideRental(anyLong());
    }

    @DisplayName("Should Throw ResourceNotFoundException When Rental Not Found")
    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    public void testHideRentalRentalNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");

        when(userRepository.findByName("testUser")).thenReturn(Optional.of(user));
        when(rentalService.findRentalByIdAndUser(1L, user.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/my_books/{rentalId}/hide", 1L)
                        .principal(() -> "testUser")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound());

        verify(rentalService, never()).hideRental(anyLong());
    }


}