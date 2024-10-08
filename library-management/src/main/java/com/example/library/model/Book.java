package com.example.library.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "The title of the book must not be empty")
    private String title;
    @Column(nullable = false)
    @NotEmpty(message = "The author of a book cannot be empty")
    private String author;
    @Column(nullable = false)
    private String isAvailable = "dostępna";
    @Column(columnDefinition = "TEXT")
    private String summary;
    @OneToMany(mappedBy = "book")
    private List<Rental> rentals;

    public Book(long l, String s, String s1) {

    }
}
