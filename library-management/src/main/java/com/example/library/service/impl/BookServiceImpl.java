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
    public Book addBook(Book book) {
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }
//
//    @Override
//    public List<Book> getBookByTitle(String title) {
//        List<Book> byTitle = bookRepository.findByTitle(title);
//        return byTitle;
//    }
//
//    @Override
//    public void deleteBookById(Long id) {
//        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book is not exists with given id: " + id));
//
//        bookRepository.deleteById(id);
//    }
//
//    @Override
//    public void deleteBookByTitle(String title) {
//        bookRepository.findByTitle(title);
//
//        bookRepository.deleteBookByTitle(title);
//    }
//
//    @Override
//    public Book updateBook(Long id, Book updatedBook) {
//        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book is not exists with given id: " + id));
//
//        book.setTitle(updatedBook.getTitle());
//        book.setAuthor(updatedBook.getAuthor());
//        book.setIsAvailable(updatedBook.getIsAvailable());
//
//        Book savedBook = bookRepository.save(book);
//
//        return savedBook;
//    }
//
//    @Override
//    public Book rentBook(Long id) {
//        Optional<Book> bookOptional = bookRepository.findById(id);
//
//        if (bookOptional.isPresent()) {
//            Book book = bookOptional.get();
//
//            if ("dostępna".equals(book.getIsAvailable())) {
//                book.setIsAvailable("wypożyczona");
//                return bookRepository.save(book);
//            } else {
//                throw new RuntimeException("Book is already rented out");
//            }
//        } else {
//            throw new ResourceNotFoundException("Book is not exists with given id: " + id);
//        }
//    }
//
//    @Override
//    public Book returnBook(Long id) {
//
//        Optional<Book> bookOptional = bookRepository.findById(id);
//
//        if (bookOptional.isPresent()) {
//            Book book = bookOptional.get();
//
//            if ("wypożyczona".equals(book.getIsAvailable())) {
//                book.setIsAvailable("dostępna");
//                return bookRepository.save(book);
//            } else {
//                throw new RuntimeException("Book is already returned");
//            }
//        } else {
//            throw new ResourceNotFoundException("Book is not exists with given id: " + id);
//        }
//   }
//
//    @Override
//    public Book getBookById(Long id) {
//        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book is not exists with given id: " + id));
//
//        return book;
//    }
}
