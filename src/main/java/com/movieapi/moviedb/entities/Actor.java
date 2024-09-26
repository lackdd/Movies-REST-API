package com.movieapi.moviedb.entities;

import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String birthDate;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> movies;

    public Actor() {}

    public Actor(String name, String birthDate, Set<Movie> movies) {
        this.name = name;
        this.birthDate = birthDate;
        this.movies = movies;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
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
    public Set<Movie> getMovies() {
        return movies;
    }
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
