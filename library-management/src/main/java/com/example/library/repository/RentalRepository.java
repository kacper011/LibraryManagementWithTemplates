package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.Rental;
import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);

    List<Rental> findByBookAndUser(Book book, User user);

    List<Rental> findByUserId(Long id);

    List<Rental> findByUser_IdAndHiddenFalse(Long userId);

    Optional<Rental> findByIdAndUserId(Long rentalId, Long id);
}
