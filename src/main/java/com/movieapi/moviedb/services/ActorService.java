package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.dto.MovieSummaryDTO;
import com.movieapi.moviedb.repositories.ActorRepository;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Create a new actor
    public ActorDTO createActor(ActorDTO actorDTO) {
        Actor actor = convertToEntity(actorDTO);
        Actor savedActor = actorRepository.save(actor);
        return convertToDTO(savedActor);
    }

    // Get all actors
    public List<ActorDTO> getAllActors() {
        return actorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get actor by ID
    public Optional<ActorDTO> getActorById(Integer id) {
        return actorRepository.findById(id).map(this::convertToDTO);
    }

    // Update an existing actor
    public ActorDTO updateActor(Integer id, ActorDTO updatedActorDTO) {
        return actorRepository.findById(id)
                .map(actor -> {
                    if (updatedActorDTO.getName() != null) {
                        actor.setName(updatedActorDTO.getName());
                    }
                    if (updatedActorDTO.getBirthDate() != null) {
                        actor.setBirthDate(updatedActorDTO.getBirthDate());
                    }
                    if (updatedActorDTO.getMovies() != null && !updatedActorDTO.getMovies().isEmpty()) {
                        Set<Movie> movies = updatedActorDTO.getMovies().stream()
                                .map(movieDTO -> movieRepository.findById(movieDTO.getId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieDTO.getId())))
                                .collect(Collectors.toSet());
                        actor.setMovies(movies);
                    }
                    return convertToDTO(actorRepository.save(actor));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    // Delete an actor
    public void deleteActor(Integer id) {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Actor not found with id: " + id);
        }
    }

    public List<ActorDTO> getActorsByName(String name) {
        List<Actor> actors = actorRepository.findByNameContainingIgnoreCase(name);
        return actors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversion methods between Actor and ActorDTO
    private ActorDTO convertToDTO(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setBirthDate(actor.getBirthDate());

        Set<MovieSummaryDTO> movieSummaries = actor.getMovies().stream()
                .map(this::convertToMovieSummaryDTO)
                .collect(Collectors.toSet());
        actorDTO.setMovies(movieSummaries);

        return actorDTO;
    }

    private Actor convertToEntity(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setName(actorDTO.getName());
        actor.setBirthDate(actorDTO.getBirthDate());

        if (actorDTO.getMovies() != null && !actorDTO.getMovies().isEmpty()) {
            Set<Movie> movies = actorDTO.getMovies().stream()
                    .map(movieDTO -> movieRepository.findById(movieDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieDTO.getId())))
                    .collect(Collectors.toSet());
            actor.setMovies(movies);
        }

        return actor;
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

    private MovieSummaryDTO convertToMovieSummaryDTO(Movie movie) {
        MovieSummaryDTO summary = new MovieSummaryDTO();
        summary.setId(movie.getId());
        summary.setTitle(movie.getTitle());
        summary.setReleaseYear(movie.getReleaseYear());
        summary.setDuration(movie.getDuration());

        Set<String> genreNames = movie.getGenres().stream()
                .map(genre -> genre.getName())
                .collect(Collectors.toSet());
        summary.setGenreNames(genreNames);
        System.out.println("Fetched Genres: " + movie.getGenres()); // Debug: Check if genres are being fetched


        Set<String> actorNames = movie.getActors().stream()
                .map(actor -> actor.getName())
                .collect(Collectors.toSet());
        summary.setActorNames(actorNames);
        //System.out.println("Actor Names: " + actorNames); // Debug

        return summary;
    }
}