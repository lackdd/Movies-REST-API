package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.dto.MovieSummaryDTO;
import com.movieapi.moviedb.repositories.ActorRepository;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ActorDTO createActor(ActorDTO actorDTO) {
        Actor actor = convertToEntity(actorDTO);
        Actor savedActor = actorRepository.save(actor);
        return convertToDTO(savedActor);
    }

    public Page<ActorDTO> getAllActors(Pageable pageable) {
        Page<Actor> actorsPage = actorRepository.findAll(pageable);
        return actorsPage.map(this::convertToDTO);
    }

    public Optional<ActorDTO> getActorById(Integer id) {
        return actorRepository.findById(id).map(this::convertToDTO);
    }

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

    public void deleteActor(Integer id, boolean force) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));

        if (!actor.getMovies().isEmpty()) {
            if (!force) {
                throw new IllegalStateException("Cannot delete actor '" + actor.getName() + "' because they are associated with " + actor.getMovies().size() + " movies.");
            } else {
                actor.getMovies().forEach(movie -> movie.getActors().remove(actor));
                actor.getMovies().clear();
            }
        }

        actorRepository.delete(actor);
    }

    public Page<ActorDTO> getActorsByName(String name, Pageable pageable) {
        Page<Actor> actorsPage = actorRepository.findByNameContainingIgnoreCase(name, pageable);
        return actorsPage.map(this::convertToDTO);
    }

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

        if (actorDTO.getMovieIds() != null && !actorDTO.getMovieIds().isEmpty()) {
            Set<Movie> movies = actorDTO.getMovieIds().stream()
                    .map(movieId -> movieRepository.findById(movieId)
                            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId)))
                    .collect(Collectors.toSet());
            actor.setMovies(movies);
        }

        return actor;
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


        Set<String> actorNames = movie.getActors().stream()
                .map(actor -> actor.getName())
                .collect(Collectors.toSet());
        summary.setActorNames(actorNames);

        return summary;
    }
}