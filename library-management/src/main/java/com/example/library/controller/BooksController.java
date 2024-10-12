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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
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


        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Authentication is null or user is not authenticated");
            return "redirect:/login";
        }

        String username = authentication.getName();
        System.out.println("Username in controller: " + username);
        model.addAttribute("username", username);

        List<Book> books = bookService.getAllBooks();


        model.addAttribute("books", books);


        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin ? "books_admin" : "books_user";
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
            return "create_book_admin";
        }

        book.setDateAdded(LocalDate.now());
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
        bookService.updateBook(book);
        return "redirect:/books";
    }
    @GetMapping("/books/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Book book = bookService.getBookById(id);

        if ("wypożyczona".equalsIgnoreCase(book.getIsAvailable())) {
            redirectAttributes.addFlashAttribute("error", "Nie można usunąć książki, która jest wypożyczona.");
            return "redirect:/books";
        }

        bookService.deleteBook(id);
        return "redirect:/books";
    }



    //ROLE USER

    @GetMapping("/books_user")
    @PreAuthorize("hasRole('USER')")
    public String getBooksUsers(Principal principal,
                                @RequestParam(value = "title", required = false) String title,
                                Model model) {

        if (principal == null) {
            throw new IllegalStateException("Principal should not be null");
        }

        String username = principal.getName();
        if (username == null) {
            throw new IllegalStateException("Username should not be null");
        }

        List<Book> books;
        if (title != null && !title.isEmpty()) {
            books = bookService.searchBooksByTitle(title);
            model.addAttribute("searchQuery", title);
        } else {
            books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        model.addAttribute("username", user.getName());

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
    public String rentBook(@PathVariable("id") Long bookId, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("Principal should not be null");
        }

        String username = principal.getName();
        if (username == null) {
            throw new IllegalStateException("Username should not be null");
        }

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        rentalService.rentBook(bookId, user.getId());
        return "redirect:/my_books";
    }




    @Secured("ROLE_USER")
    @PostMapping("/books/{id}/return")
    public String returnBook(@PathVariable("id") Long bookId, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("Principal should not be null");
        }

        String username = principal.getName();
        if (username == null) {
            throw new IllegalStateException("Username should not be null");
        }

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        rentalService.returnBook(bookId, user.getId());
        return "redirect:/my_books";
    }



    @GetMapping("/my_books")
    @PreAuthorize("hasRole('USER')")
    public String listMyBooks(Principal principal, Model model) {
        if (principal == null) {
            throw new IllegalStateException("Principal should not be null");
        }

        String username = principal.getName();
        if (username == null) {
            throw new IllegalStateException("Username should not be null");
        }

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Rental> rentals = rentalService.findRentalsByUser(user.getId());
        Collections.reverse(rentals);

        model.addAttribute("username", user.getName());
        model.addAttribute("rentals", rentals);

        return "my_books";
    }




    @PostMapping("/my_books/{rentalId}/hide")
    @PreAuthorize("hasRole('USER')")
    public String hideRental(@PathVariable Long rentalId, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User not logged in");
        }

        String username = principal.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Rental rental = rentalService.findRentalByIdAndUser(rentalId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        rentalService.hideRental(rentalId);

        return "redirect:/my_books";
    }




}
