package com.movieapi.moviedb.dto;

import java.util.Set;

public class GenreSummaryDTO {
    private Integer id;
    private String name;
    private Set<String> movieNames;

    public GenreSummaryDTO() {}

    public GenreSummaryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Set<String> getMovieNames() {
        return movieNames;
    }

    public void setMovieNames(Set<String> movieNames) {
        this.movieNames = movieNames;
    }
}