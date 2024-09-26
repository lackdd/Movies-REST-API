package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.entities.Actor;
import com.movieapi.moviedb.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.createActor(actor);
    }

    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getActorById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found"));
    }
    @PatchMapping("/{id}")
    public Actor updateActor(@PathVariable Long id, @RequestBody Actor updatedActor) {
        return actorService.updateActor(id, updatedActor);
    }
    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
    }
}
