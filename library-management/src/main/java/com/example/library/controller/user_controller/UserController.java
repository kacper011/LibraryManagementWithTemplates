package com.example.library.controller.user_controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController {


    private BookService bookService;

    public UserController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "user/books_users";
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
        return "redirect:user/books";
    }

    @GetMapping("books/{id}/return")
    public String returnBook(@PathVariable("id") Long id) {
        bookService.returnBook(id);
        return "redirect:user/books";
    }
}
