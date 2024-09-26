package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            movie.setTitle(updatedMovie.getTitle());
            movie.setReleaseYear(updatedMovie.getReleaseYear());
            movie.setDuration(updatedMovie.getDuration());
            movie.setGenre(updatedMovie.getGenre());
            return movieRepository.save(movie);
        }
        return null;
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
