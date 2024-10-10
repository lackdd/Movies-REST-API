package com.movieapi.moviedb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public class ActorDTO {
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotNull(message = "Birth date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birth date must be in the format YYYY-MM-DD")
    private String birthDate;
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