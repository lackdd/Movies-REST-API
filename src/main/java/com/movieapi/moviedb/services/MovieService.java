package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.ActorSummaryDTO;
import com.movieapi.moviedb.dto.GenreSummaryDTO;
import com.movieapi.moviedb.repositories.ActorRepository;
import com.movieapi.moviedb.repositories.MovieRepository;
import com.movieapi.moviedb.repositories.GenreRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

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

    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        return moviesPage.map(this::convertToDTO);
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

                    // Update genres
                    if (updatedMovieDTO.getGenreIds() != null && !updatedMovieDTO.getGenreIds().isEmpty()) {
                        Set<Genre> genres = updatedMovieDTO.getGenreIds().stream()
                                .map(genreId -> genreRepository.findById(genreId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreId)))
                                .collect(Collectors.toSet());
                        movie.setGenres(genres);
                    }

                    // Update actors: clear the existing actors and set new ones
                    if (updatedMovieDTO.getActorIds() != null && !updatedMovieDTO.getActorIds().isEmpty()) {
                        // First, clear the current list of actors in the movie
                        Set<Actor> currentActors = new HashSet<>(movie.getActors());
                        for (Actor actor : currentActors) {
                            actor.getMovies().remove(movie);  // Remove the movie from the actor's movie list (bidirectional)
                        }
                        movie.getActors().clear();  // Clear the movie's actor list

                        // Now, add the new actors from actorIds
                        Set<Actor> newActors = updatedMovieDTO.getActorIds().stream()
                                .map(actorId -> actorRepository.findById(actorId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId)))
                                .collect(Collectors.toSet());
                        movie.setActors(newActors);

                        // Maintain the bidirectional relationship
                        for (Actor newActor : newActors) {
                            newActor.getMovies().add(movie);  // Add the movie to each new actor's movie list
                        }
                    }

                    return convertToDTO(movieRepository.save(movie));  // Save the updated movie
                })
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public void deleteMovie(Integer id, boolean force) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        if (!movie.getActors().isEmpty() || !movie.getGenres().isEmpty()) {
            if (!force) {
                throw new IllegalStateException("Cannot delete movie '" + movie.getTitle() + "' because it is associated with actors or genres.");
            } else {
                // Remove relationships before deletion
                movie.getActors().forEach(actor -> actor.getMovies().remove(movie));
                movie.getActors().clear();

                movie.getGenres().forEach(genre -> genre.getMovies().remove(movie));
                movie.getGenres().clear();
            }
        }

        movieRepository.delete(movie);
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

    public MovieDTO removeActorFromMovie(Integer movieId, Integer actorId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        System.out.println("Movie actors: " + movie.getActors());
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId));

        // Remove the actor if they are currently associated with the movie
        if (movie.getActors().contains(actor)) {
            movie.getActors().remove(actor);
            actor.getMovies().remove(movie);
        } else {
            throw new ResourceNotFoundException("Actor with id: " + actorId + " is not associated with movie id: " + movieId);
        }

        Movie updatedMovie = movieRepository.save(movie);

        return convertToDTO(updatedMovie);
    }

    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        return moviesPage.map(this::convertToDTO);
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setDuration(movie.getDuration());

        // Convert Genres to GenreSummaryDTO objects (for retrieval)
        Set<GenreSummaryDTO> genreSummaries = movie.getGenres().stream()
                .map(this::convertToGenreSummaryDTO)
                .collect(Collectors.toSet());
        movieDTO.setGenres(genreSummaries);

        // Convert Actors to ActorSummaryDTO objects (for retrieval)
        Set<ActorSummaryDTO> actorSummaries = movie.getActors().stream()
                .map(this::convertToActorSummaryDTO)
                .collect(Collectors.toSet());
        movieDTO.setActors(actorSummaries);

        return movieDTO;
    }

    private GenreSummaryDTO convertToGenreSummaryDTO(Genre genre) {
        GenreSummaryDTO genreSummaryDTO = new GenreSummaryDTO();
        genreSummaryDTO.setId(genre.getId());
        genreSummaryDTO.setName(genre.getName());

        Set<String> movieNames = genre.getMovies().stream()
                .map(Movie::getTitle)
                .collect(Collectors.toSet());
        genreSummaryDTO.setMovieNames(movieNames);
        return genreSummaryDTO;
    }

    private ActorSummaryDTO convertToActorSummaryDTO(Actor actor) {
        ActorSummaryDTO actorSummaryDTO = new ActorSummaryDTO();
        actorSummaryDTO.setId(actor.getId());
        actorSummaryDTO.setName(actor.getName());

        Set<String> movieNames = actor.getMovies().stream()
                .map(Movie::getTitle)
                .collect(Collectors.toSet());
        actorSummaryDTO.setMovieNames(movieNames);
        return actorSummaryDTO;
    }

    public Page<MovieDTO> getMoviesByYear(String year, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByReleaseYear(year, pageable);
        return moviesPage.map(this::convertToDTO);
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setDuration(movieDTO.getDuration());

        // Handle Genres from genreIds (for updating)
        Set<Genre> genres = new HashSet<>();
        if (movieDTO.getGenreIds() != null && !movieDTO.getGenreIds().isEmpty()) {
            genres = movieDTO.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreId)))
                    .collect(Collectors.toSet());
        }
        movie.setGenres(genres);

        // Handle Actors from actorIds (for updating)
        Set<Actor> actors = new HashSet<>();
        if (movieDTO.getActorIds() != null && !movieDTO.getActorIds().isEmpty()) {
            actors = movieDTO.getActorIds().stream()
                    .map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId)))
                    .collect(Collectors.toSet());
        }
        movie.setActors(actors);

        return movie;
    }

    public Page<MovieDTO> getMoviesByActorId(Integer actorId, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByActorId(actorId, pageable);
        return moviesPage.map(this::convertToDTO);
    }

    private ActorDTO convertToActorDTO(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setBirthDate(actor.getBirthDate());
        return actorDTO;
    }

    public List<ActorDTO> getActorsByMovieId(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        return movie.getActors().stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    public Page<MovieDTO> getMoviesByGenre(Integer genreId, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByGenreId(genreId, pageable);
        return moviesPage.map(this::convertToDTO);
    }
}