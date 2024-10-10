package com.movieapi.moviedb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class GenreDTO {
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    private Set<MovieSummaryDTO> movies;

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

    public Set<MovieSummaryDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieSummaryDTO> movies) {
        this.movies = movies;
    }
}