package com.example.library.service;

import com.example.library.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book addBook(Book book);

    List<Book> getBookByTitle(String title);

    void deleteBookById(Long id);

    void deleteBookByTitle(String title);

    Book updateBook(Long id, Book book);

    Book rentBook(Long id);

    Book returnBook(Long id);

    Book getBookById(Long id);
}
