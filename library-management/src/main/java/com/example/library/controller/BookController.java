package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
//@RequestMapping("/api/books")
public class BookController {


    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
//        Book bookById = bookService.getBookById(id);
//        return new ResponseEntity<>(bookById, HttpStatus.OK);
//    }
//
    @PostMapping("/books")
    public String addBook(Book book) {
        Book savedBook = bookService.addBook(book);
        return "redirect:/books";
    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> getBookByTitle(@RequestParam String title) {
//        List<Book> bookByTitle = bookService.getBookByTitle(title);
//        return new ResponseEntity<>(bookByTitle, HttpStatus.OK);
//
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteBookById(@PathVariable("id") Long id) {
//        bookService.deleteBookById(id);
//
//        return new ResponseEntity<>("Book deleted", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteBookByTitle(@RequestParam String title) {
//        bookService.deleteBookByTitle(title);
//
//        return new ResponseEntity<>("Book deleted", HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book updatedBook) {
//        Book book = bookService.updateBook(id, updatedBook);
//
//        return new ResponseEntity<>(book, HttpStatus.OK);
//    }
//
//    @GetMapping("/rent/{id}")
//    public ResponseEntity<Book> rentBook(@PathVariable("id") Long id) {
//
//        try {
//            Book rentedBook = bookService.rentBook(id);
//            return new ResponseEntity<>(rentedBook, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/return/{id}")
//    public ResponseEntity<Book> returnBook(@PathVariable("id") Long id) {
//
//        try {
//            Book returnedBook = bookService.returnBook(id);
//            return new ResponseEntity<>(returnedBook, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
