package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.services.ActorService;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping
    public ResponseEntity<ActorDTO> createActor(@RequestBody ActorDTO actorDTO) {
        ActorDTO createdActor = actorService.createActor(actorDTO);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActorDTO>> getAllActors() {
        List<ActorDTO> actors = actorService.getAllActors();
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Integer id) {
        return actorService.getActorById(id)
                .map(actor -> new ResponseEntity<>(actor, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Integer id, @RequestBody ActorDTO updatedActorDTO) {
        ActorDTO updatedActor = actorService.updateActor(id, updatedActorDTO);
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}