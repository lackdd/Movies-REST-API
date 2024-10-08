package com.movieapi.moviedb.dto;

import java.util.Set;

public class GenreDTO {
    private Integer id;
    private String name;
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