package com.movieapi.moviedb.dto;

import java.util.Set;

public class MovieSummaryDTO {
    private Integer id;
    private String title;
    private String releaseYear;
    private String duration;
    private Set<String> genreNames;
    private Set<String> actorNames;


    public MovieSummaryDTO() {
    }

    public MovieSummaryDTO(Integer id, String title, String releaseYear, String duration) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseYear() { return releaseYear; }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDuration() { return duration; }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(Set<String> genreNames) {
        this.genreNames = genreNames;
    }

    public Set<String> getActorNames() {
        return actorNames;
    }

    public void setActorNames(Set<String> actorNames) {
        this.actorNames = actorNames;
    }
}