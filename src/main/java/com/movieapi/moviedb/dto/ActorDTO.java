package com.movieapi.moviedb.dto;

import java.util.Set;

public class ActorDTO {
    private Integer id;
    private String name;
    private String birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Set<MovieSummaryDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieSummaryDTO> movies) {
        this.movies = movies;
    }
}