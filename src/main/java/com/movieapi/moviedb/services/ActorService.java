package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.repositories.ActorRepository;
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
        Optional<Actor> actorOptional = actorRepository.findById(id);
        if (actorOptional.isPresent()) {
            Actor actor = actorOptional.get();
            actor.setName(updatedActor.getName());
            actor.setBirthDate(updatedActor.getBirthDate());
            actor.setMovies(updatedActor.getMovies());
            return actorRepository.save(actor);
        }
        return null;
    }

    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }
}
