package com.movieapi.moviedb.dto;

import java.util.Set;

public class GenreDTO {
    private Integer id;
    private String name;
    private Set<MovieDTO> movies;

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

    public Set<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieDTO> movies) {
        this.movies = movies;
    }
}