package com.movieapi.moviedb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.Set;

public class GenreDTO {
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Movies cannot be null")
    @Size(min = 1, message = "There must be at least one movie in the genre")
    private Set<MovieSummaryDTO> movies;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MovieSummaryDTO> getMovies() {  // Updated getter and setter to use MovieSummaryDTO
        return movies;
    }

    public void setMovies(Set<MovieSummaryDTO> movies) {
        this.movies = movies;
    }
}