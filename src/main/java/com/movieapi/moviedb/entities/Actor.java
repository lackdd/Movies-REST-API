package com.movieapi.moviedb.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.*;

@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String birthDate;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonBackReference
    private Set<Movie> movies = new HashSet<>();

    public Actor() {}

    public Actor(String name, String birthDate, Set<Movie> movies) {
        this.name = name;
        this.birthDate = birthDate;
        this.movies = movies;
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

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getActors().add(this);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getActors().remove(this);
    }
}
