package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.dto.GenreDTO;
import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.repositories.ActorRepository;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.repositories.GenreRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private GenreRepository genreRepository;

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return convertToDTO(movie);
    }

    public MovieDTO updateMovie(Integer id, MovieDTO updatedMovieDTO) {
        return movieRepository.findById(id)
                .map(movie -> {
                    if (updatedMovieDTO.getTitle() != null) {
                        movie.setTitle(updatedMovieDTO.getTitle());
                    }
                    if (updatedMovieDTO.getReleaseYear() != null) {
                        movie.setReleaseYear(updatedMovieDTO.getReleaseYear());
                    }
                    if (updatedMovieDTO.getDuration() != null) {
                        movie.setDuration(updatedMovieDTO.getDuration());
                    }
                    if (updatedMovieDTO.getGenres() != null && !updatedMovieDTO.getGenres().isEmpty()) {
                        Set<Genre> genres = updatedMovieDTO.getGenres().stream()
                                .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreDTO.getId())))
                                .collect(Collectors.toSet());
                        movie.setGenres(genres);
                    }
                    if (updatedMovieDTO.getActors() != null && !updatedMovieDTO.getActors().isEmpty()) {
                        Set<Actor> actors = updatedMovieDTO.getActors().stream()
                                .map(actorDTO -> actorRepository.findById(actorDTO.getId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorDTO.getId())))
                                .collect(Collectors.toSet());
                        movie.setActors(actors);
                    }
                    return convertToDTO(movieRepository.save(movie));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public void deleteMovie(Integer id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
    }

    public MovieDTO addActorToMovie(Integer movieId, Integer actorId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId));

        if (!movie.getActors().contains(actor)) {
            movie.addActor(actor);
        }

        Movie updatedMovie = movieRepository.save(movie);
        return convertToDTO(updatedMovie);
    }

    // Conversion methods
    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setDuration(movie.getDuration());

        // Convert Genres to GenreDTOs
        movieDTO.setGenres(movie.getGenres().stream()
                .map(this::convertToGenreDTO)
                .collect(Collectors.toSet()));

        // Convert Actors to ActorDTOs
        movieDTO.setActors(movie.getActors().stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toSet()));

        return movieDTO;
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setDuration(movieDTO.getDuration());

        if (movieDTO.getGenres() != null && !movieDTO.getGenres().isEmpty()) {
            Set<Genre> genres = movieDTO.getGenres().stream()
                    .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreDTO.getId())))
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }

        if (movieDTO.getActors() != null && !movieDTO.getActors().isEmpty()) {
            Set<Actor> actors = movieDTO.getActors().stream()
                    .map(actorDTO -> actorRepository.findById(actorDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorDTO.getId())))
                    .collect(Collectors.toSet());
            movie.setActors(actors);
        }

        return movie;
    }

    private GenreDTO convertToGenreDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        // You can add other details if needed
        return genreDTO;
    }

    private ActorDTO convertToActorDTO(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setBirthDate(actor.getBirthDate());
        // You can add other details if needed
        return actorDTO;
    }
}