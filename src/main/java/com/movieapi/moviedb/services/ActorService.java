package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.repositories.ActorRepository;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Optional<Actor> getActorById(Long id) {
        return actorRepository.findById(id);
    }

    public Actor updateActor(Long id, Actor updatedActor) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setName(updatedActor.getName());
                    actor.setBirthDate(updatedActor.getBirthDate());
                    actor.setMovies(updatedActor.getMovies());
                    return actorRepository.save(actor);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    public void deleteActor(Long id) {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Actor not found with id: " + id);
        }
    }
}
