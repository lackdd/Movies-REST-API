package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
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
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setReleaseYear(updatedMovie.getReleaseYear());
                    movie.setDuration(updatedMovie.getDuration());
                    movie.setGenre(updatedMovie.getGenre());
                    return movieRepository.save(movie);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id:" + id));

    }

    public void deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Movie not found with id:" + id);
        }
    }
}
