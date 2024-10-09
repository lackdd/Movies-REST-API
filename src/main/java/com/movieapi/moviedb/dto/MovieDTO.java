package com.movieapi.moviedb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieDTO {
    private Integer id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    @NotNull(message = "Release year cannot be null")
    @Size(min = 4, max = 4, message = "Release year must be 4 digits")
    private String releaseYear;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private String duration;
    private Set<GenreSummaryDTO> genres;
    private Set<ActorSummaryDTO> actors;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> genreIds;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> actorIds;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<GenreSummaryDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreSummaryDTO> genres) {
        this.genres = genres;
    }

    public Set<ActorSummaryDTO> getActors() {
        return actors;
    }

    public void setActors(Set<ActorSummaryDTO> actors) {
        this.actors = actors;
    }

    public Set<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Set<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Set<Integer> getActorIds() {
        return actorIds;
    }

    public void setActorIds(Set<Integer> actorIds) {
        this.actorIds = actorIds;
    }
}