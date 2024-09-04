package com.example.library.service;

import com.example.library.model.Rental;
import com.example.library.model.User;

import java.util.List;
import java.util.Optional;

public interface RentalService {

    void rentBook(Long bookId, Long userId);

    void returnBook(Long bookId, Long userId);

    List<Rental> findRentalsByUser(Long id);

    void hideRental(Long rentalId);

    Optional<Rental> findRentalByIdAndUser(Long rentalId, Long id);
}
