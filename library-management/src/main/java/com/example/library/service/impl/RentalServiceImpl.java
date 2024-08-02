package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentalRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.RentalService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        book.setIsAvailable("wypożyczona");
        bookRepository.save(book);

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentalDate(now);
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void returnBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Rental rental = rentalRepository.findByBookAndUser(book, user)
                .stream()
                .filter(r -> r.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active rental record found for this book and user"));

        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("The book has already been returned");
        }

        rental.setReturnDate(LocalDateTime.now().withSecond(0).withNano(0));
        rentalRepository.save(rental);

        book.setIsAvailable("dostępna");
        bookRepository.save(book);
    }

    @Override
    public List<Rental> findRentalsByUser(Long id) {
        return rentalRepository.findByUserId(id);
    }


}
