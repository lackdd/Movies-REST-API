package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.dto.GenreDTO;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.repositories.GenreRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Create a new genre
    public GenreDTO createGenre(GenreDTO genreDTO) {
        Genre genre = convertToEntity(genreDTO);
        Genre savedGenre = genreRepository.save(genre);
        return convertToDTO(savedGenre);
    }

    // Get all genres
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get genre by ID
    public Optional<GenreDTO> getGenreById(Integer id) {
        return genreRepository.findById(id).map(this::convertToDTO);
    }

    // Update an existing genre
    public GenreDTO updateGenre(Integer id, GenreDTO updatedGenreDTO) {
        return genreRepository.findById(id)
                .map(genre -> {
                    if (updatedGenreDTO.getName() != null) {
                        genre.setName(updatedGenreDTO.getName());
                    }
                    if (updatedGenreDTO.getMovies() != null && !updatedGenreDTO.getMovies().isEmpty()) {
                        Set<Movie> movies = updatedGenreDTO.getMovies().stream()
                                .map(movieDTO -> movieRepository.findById(movieDTO.getId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieDTO.getId())))
                                .collect(Collectors.toSet());
                        genre.setMovies(movies);
                    }
                    return convertToDTO(genreRepository.save(genre));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    // Delete a genre
    public void deleteGenre(Integer id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Genre not found with id: " + id);
        }
    }

    // Conversion methods between Genre and GenreDTO
    private GenreDTO convertToDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        genreDTO.setMovies(genre.getMovies().stream()
                .map(this::convertToMovieDTO)
                .collect(Collectors.toSet()));
        return genreDTO;
    }

    private Genre convertToEntity(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setName(genreDTO.getName());

        if (genreDTO.getMovies() != null && !genreDTO.getMovies().isEmpty()) {
            Set<Movie> movies = genreDTO.getMovies().stream()
                    .map(movieDTO -> movieRepository.findById(movieDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieDTO.getId())))
                    .collect(Collectors.toSet());
            genre.setMovies(movies);
        }

        return genre;
    }

    // Helper method to convert Movie to MovieDTO
    private MovieDTO convertToMovieDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setDuration(movie.getDuration());
        return movieDTO;
    }
}