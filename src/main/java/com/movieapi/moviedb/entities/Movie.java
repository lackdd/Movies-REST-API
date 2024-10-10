package com.movieapi.moviedb.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String releaseYear;
    private String duration;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonManagedReference
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(mappedBy = "movies")
    @JsonManagedReference
    private Set<Actor> actors = new HashSet<>();

    public Movie() {}

    public Movie(String title, String releaseYear, String duration, Set<Genre> genres) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.genres = genres;
        this.actors = new HashSet<>();
    }

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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
        actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.getMovies().remove(this);
    }
}