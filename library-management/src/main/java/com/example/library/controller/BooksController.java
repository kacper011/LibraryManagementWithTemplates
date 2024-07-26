package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BooksController {


    private BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String getAllBooks(Model model, Authentication authentication) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

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

    @GetMapping("/books/{id}/view")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "view_book";
    }

    @GetMapping("books/{id}/rent")
    public String rentBook(@PathVariable("id") Long id) {
        bookService.rentBook(id);
        return "redirect:/books";
    }

    @GetMapping("books/{id}/return")
    public String returnBook(@PathVariable("id") Long id) {
        bookService.returnBook(id);
        return "redirect:/books";
    }
}
