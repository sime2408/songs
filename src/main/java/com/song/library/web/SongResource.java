package com.song.library.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.song.library.dto.SongDTO;
import com.song.library.repository.SongRepository;
import com.song.library.service.SongService;

@RestController
@RequestMapping("/api/songs")
public class SongResource {

    private final Logger log = LoggerFactory.getLogger(SongResource.class);

    private static final String ENTITY_NAME = "song";

    private final SongService songService;

    private final SongRepository songRepository;

    public SongResource(SongService songService, SongRepository songRepository) {
        this.songService = songService;
        this.songRepository = songRepository;
    }

    @PostMapping("")
    public ResponseEntity<SongDTO> createSong(@RequestBody SongDTO songDTO) throws URISyntaxException {
        log.debug("REST request to save Song : {}", songDTO);
        if (songDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new song cannot already have an ID"+ ENTITY_NAME + "idexists");
        }
        SongDTO result = songService.save(songDTO);
        return ResponseEntity
            .created(new URI("/api/songs/" + result.getId()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongDTO> updateSong(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SongDTO songDTO
    ) {
        log.debug("REST request to update Song : {}, {}", id, songDTO);
        if (songDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id"+ ENTITY_NAME + "idnull");
        }
        if (!Objects.equals(id, songDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID"+ ENTITY_NAME + "idinvalid");
        }

        if (!songRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found"+ ENTITY_NAME + "idnotfound");
        }

        SongDTO result = songService.update(songDTO);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable("id") Long id) {
        log.debug("REST request to get Song : {}", id);
        Optional<SongDTO> songDTO = songService.findOne(id);
        return ResponseUtil.wrapOrNotFound(songDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") Long id) {
        log.debug("REST request to delete Song : {}", id);
        songService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
