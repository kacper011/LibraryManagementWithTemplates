package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentalRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.RentalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    private RentalRepository rentalRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void rentBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (!"dostępna".equals(book.getIsAvailable())) {
            throw new IllegalStateException("Book is already rented");
        }

        book.setIsAvailable("wypożyczona");
        bookRepository.save(book);

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentalDate(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    @Override
    public void returnBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Rental rental = (Rental) rentalRepository.findByBookAndUser(book, user)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        book.setIsAvailable("dostępna");
        bookRepository.save(book);

        rental.setReturnDate(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    @Override
    public List<Rental> findRentalsByUser(Long id) {
        return rentalRepository.findByUserId(id);
    }


}
