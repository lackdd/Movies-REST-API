package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.dto.GenreDTO;
import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.ActorSummaryDTO;
import com.movieapi.moviedb.dto.GenreSummaryDTO;
import com.movieapi.moviedb.dto.MovieSummaryDTO;
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
        // Find the movie by ID or throw an exception if not found
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        System.out.println("Movie actors: " + movie.getActors());
        // Find the actor by ID or throw an exception if not found
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId));

        // Remove the actor if they are currently associated with the movie
        if (movie.getActors().contains(actor)) {
            movie.getActors().remove(actor);  // Remove the actor from the movie's actor list
            actor.getMovies().remove(movie);  // Optionally remove the movie from the actor's movie list to maintain bidirectional consistency
        } else {
            throw new ResourceNotFoundException("Actor with id: " + actorId + " is not associated with movie id: " + movieId);
        }

        // Save the updated movie entity
        Movie updatedMovie = movieRepository.save(movie);

        // Convert the updated movie entity to a MovieDTO and return it
        return convertToDTO(updatedMovie);
    }

    // Conversion methods
    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setDuration(movie.getDuration());

        // Convert Genres to GenreSummaryDTOs
        Set<GenreSummaryDTO> genreSummaries = movie.getGenres().stream()
                .map(this::convertToGenreSummaryDTO)
                .collect(Collectors.toSet());
        movieDTO.setGenres(genreSummaries);

        // Convert Actors to ActorSummaryDTOs
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
        // Populate movie names for the genre
        Set<String> movieNames = genre.getMovies().stream()
                .map(Movie::getTitle)  // Get movie titles
                .collect(Collectors.toSet());
        genreSummaryDTO.setMovieNames(movieNames);
        return genreSummaryDTO;
    }

    private ActorSummaryDTO convertToActorSummaryDTO(Actor actor) {
        ActorSummaryDTO actorSummaryDTO = new ActorSummaryDTO();
        actorSummaryDTO.setId(actor.getId());
        actorSummaryDTO.setName(actor.getName());
        // Populate movie names for the actor
        Set<String> movieNames = actor.getMovies().stream()
                .map(Movie::getTitle)  // Get movie titles
                .collect(Collectors.toSet());
        actorSummaryDTO.setMovieNames(movieNames);
        return actorSummaryDTO;
    }

    public List<MovieDTO> getMoviesByYear(String year) {
        List<Movie> movies = movieRepository.findByReleaseYear(year);
        return movies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setDuration(movieDTO.getDuration());

        // Handle Genres from genreIds (deserialization)
        Set<Genre> genres = new HashSet<>();
        if (movieDTO.getGenreIds() != null && !movieDTO.getGenreIds().isEmpty()) {
            genres = movieDTO.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreId)))
                    .collect(Collectors.toSet());
        }
        movie.setGenres(genres);

        // Handle Actors from actorIds (deserialization)
        Set<Actor> actors = new HashSet<>();
        if (movieDTO.getActorIds() != null && !movieDTO.getActorIds().isEmpty()) {
            actors = movieDTO.getActorIds().stream()
                    .map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId)))
                    .collect(Collectors.toSet());
            for (Actor actor : actors) {
                actor.getMovies().add(movie);  // Add movie to the actor's list of movies
            }
        }
        movie.setActors(actors);

        return movie;
    }

    private GenreDTO convertToGenreDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        // You can add other details if needed
        return genreDTO;
    }

    public List<MovieDTO> getMoviesByActorId(Integer actorId) {
        List<Movie> movies = movieRepository.findByActorId(actorId);
        return movies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ActorDTO convertToActorDTO(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setBirthDate(actor.getBirthDate());
        // You can add other details if needed
        return actorDTO;
    }

    public List<ActorDTO> getActorsByMovieId(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        return movie.getActors().stream()
                .map(this::convertToActorDTO)  // Use a method to convert Actor entities to ActorDTO
                .collect(Collectors.toList());
    }

    private MovieSummaryDTO convertToMovieSummaryDTO(Movie movie) {
        MovieSummaryDTO summaryDTO = new MovieSummaryDTO();
        summaryDTO.setId(movie.getId());
        summaryDTO.setTitle(movie.getTitle());
        summaryDTO.setReleaseYear(movie.getReleaseYear());
        summaryDTO.setDuration(movie.getDuration());

        // Extract genre names
        Set<String> genreNames = movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
        summaryDTO.setGenreNames(genreNames);

        // Extract actor names
        Set<String> actorNames = movie.getActors().stream()
                .map(Actor::getName)
                .collect(Collectors.toSet());
        summaryDTO.setActorNames(actorNames);

        return summaryDTO;
    }

    public List<MovieDTO> getMoviesByGenre(Integer genreId) {
        List<Movie> movies = movieRepository.findByGenreId(genreId);
        return movies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}