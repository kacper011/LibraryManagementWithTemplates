package com.example.library.service;

import com.example.library.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    void createBook(Book book);

    Book getBookById(Long id);

    void updateStudent(Book book);
}
