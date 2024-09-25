package com.example.library.service;

import com.example.library.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    void createBook(Book book);

    Book getBookById(Long id);

    void updateBook(Book book);

    void deleteBook(Long id);

    void rentBook(Long id);

    void returnBook(Long id);

    List<Book> searchBooksByTitle(String title);
}
