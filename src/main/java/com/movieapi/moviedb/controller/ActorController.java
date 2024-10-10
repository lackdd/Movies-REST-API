package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.dto.ActorDTO;
import com.movieapi.moviedb.services.ActorService;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping
    public ResponseEntity<ActorDTO> createActor(@Valid @RequestBody ActorDTO actorDTO) {
        ActorDTO createdActor = actorService.createActor(actorDTO);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ActorDTO>> getAllActors(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (name != null) {
            Page<ActorDTO> actors = actorService.getActorsByName(name, pageable);
            return new ResponseEntity<>(actors, HttpStatus.OK);
        }

        Page<ActorDTO> actors = actorService.getAllActors(pageable);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Integer id) {
        return actorService.getActorById(id)
                .map(actor -> new ResponseEntity<>(actor, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Integer id, @Valid @RequestBody ActorDTO updatedActorDTO) {
        ActorDTO updatedActor = actorService.updateActor(id, updatedActorDTO);
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}