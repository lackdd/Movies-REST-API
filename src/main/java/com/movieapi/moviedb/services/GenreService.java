package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.dto.GenreDTO;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.MovieSummaryDTO;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.repositories.GenreRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<GenreDTO> getAllGenres(Pageable pageable) {
        Page<Genre> genresPage = genreRepository.findAll(pageable);
        return genresPage.map(this::convertToDTO);
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
    public void deleteGenre(Integer id, boolean force) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        // Check if the genre is associated with any movies
        if (!genre.getMovies().isEmpty()) {
            if (!force) {
                throw new IllegalStateException("Cannot delete genre '" + genre.getName() + "' because it has " + genre.getMovies().size() + " associated movies.");
            } else {
                // Remove relationships before deletion
                genre.getMovies().forEach(movie -> movie.getGenres().remove(genre));
                genre.getMovies().clear();
            }
        }

        genreRepository.delete(genre);
    }

    // Conversion methods between Genre and GenreDTO
    private GenreDTO convertToDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        // Convert the movies under this genre to MovieSummaryDTO
        genreDTO.setMovies(genre.getMovies().stream()
                .map(this::convertToMovieSummaryDTO)  // Make sure to call the MovieSummaryDTO converter
                .collect(Collectors.toSet()));
        return genreDTO;
    }

    private MovieSummaryDTO convertToMovieSummaryDTO(Movie movie) {
        MovieSummaryDTO movieSummaryDTO = new MovieSummaryDTO();
        movieSummaryDTO.setId(movie.getId());
        movieSummaryDTO.setTitle(movie.getTitle());
        movieSummaryDTO.setReleaseYear(movie.getReleaseYear());
        movieSummaryDTO.setDuration(movie.getDuration());

        // Extract genre names
        Set<String> genreNames = movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
        movieSummaryDTO.setGenreNames(genreNames);

        // Extract actor names
        Set<String> actorNames = movie.getActors().stream()
                .map(Actor::getName)
                .collect(Collectors.toSet());
        movieSummaryDTO.setActorNames(actorNames);

        return movieSummaryDTO;
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