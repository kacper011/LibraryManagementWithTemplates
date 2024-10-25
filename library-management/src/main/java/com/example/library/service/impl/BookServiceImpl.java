package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Override
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookRepository.findById(id).get();
        return book;
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void rentBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if("wypożyczona".equals(book.getIsAvailable())) {
            throw new IllegalStateException("Book is already rented");
        }

        book.setIsAvailable("wypożyczona");
        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setIsAvailable("dostępna");
        bookRepository.save(book);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {

        if(title == null) {
            return List.of();
        }

        String normalizedTitle = title.trim().replaceAll("\\s+", " ");
        return bookRepository.findByTitleContainingIgnoreCase(normalizedTitle);
    }


}
