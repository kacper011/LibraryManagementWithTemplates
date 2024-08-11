package com.example.library.controller;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.BookService;
import com.example.library.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class BooksController {


    private BookService bookService;

    private RentalService rentalService;

    private UserRepository userRepository;

    public BooksController(BookService bookService, RentalService rentalService, UserRepository userRepository) {
        this.bookService = bookService;
        this.rentalService = rentalService;
        this.userRepository = userRepository;
    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String getAllBooks(Model model, Authentication authentication) {

        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = "Guest";
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            model.addAttribute("username", username);
        }

        boolean hasAdminRole = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean hasUserRole = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (hasAdminRole) {
            return "books_admin";
        } else if (hasUserRole) {
            return "books_user";
        } else {
            return "access_denied";
        }
    }

    //ROLE_ADMIN

    @GetMapping("/books_admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getBooksAdmin(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books_admin";
    }


    @GetMapping("/books/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "create_book_admin";
    }

    @PostMapping("/books")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("book", book);
            return "create_book";
        }

        bookService.createBook(book);
        return "redirect:/books_admin";
    }
    @GetMapping("/books/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "edit_book_admin";
    }

    @PostMapping("/books/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateBook(@PathVariable("id") Long id,@Valid @ModelAttribute Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("books", book);
            return "edit_book_admin";
        }

        book.setId(id);
        bookService.updateStudent(book);
        return "redirect:/books";
    }
    @GetMapping("/books/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    //ROLE USER

    @GetMapping("/books_user")
    @PreAuthorize("hasRole('USER')")
    public String getBooksUsers(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books_user";
    }


    @GetMapping("/books/{id}/view")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "view_book";
    }
    @Secured("ROLE_USER")
    @GetMapping("/books/{id}/rent")
    public String rentBook(@PathVariable("id") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        rentalService.rentBook(bookId, user.getId());
        return "redirect:/my_books";
    }
    @Secured("ROLE_USER")
    @PostMapping("/books/{id}/return")
    public String returnBook(@PathVariable("id") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        rentalService.returnBook(bookId, user.getId());
        return "redirect:/my_books";
    }
    @GetMapping("/my_books")
    @PreAuthorize("hasRole('USER')")
    public String listMyBooks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Rental> rentals = rentalService.findRentalsByUser(user.getId());
        Collections.reverse(rentals);
        model.addAttribute("rentals", rentals);
        return "my_books";
    }
}
