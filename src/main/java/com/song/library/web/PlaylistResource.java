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

import com.song.library.dto.PlaylistDTO;
import com.song.library.repository.PlaylistRepository;
import com.song.library.service.PlaylistService;


@RestController
@RequestMapping("/api/playlists")
public class PlaylistResource {

    private final Logger log = LoggerFactory.getLogger(PlaylistResource.class);

    private static final String ENTITY_NAME = "playlist";

    private final PlaylistService playlistService;

    private final PlaylistRepository playlistRepository;


    public PlaylistResource(
        PlaylistService playlistService,
        PlaylistRepository playlistRepository
    ) {
        this.playlistService = playlistService;
        this.playlistRepository = playlistRepository;
    }

    @PostMapping("")
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestBody PlaylistDTO playlistDTO) throws URISyntaxException {
        log.debug("REST request to save Playlist : {}", playlistDTO);
        if (playlistDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new playlist cannot already have an ID"+ ENTITY_NAME + "idexists");
        }
        PlaylistDTO result = playlistService.save(playlistDTO);
        return ResponseEntity
            .created(new URI("/api/playlists/" + result.getId()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlaylistDTO playlistDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Playlist : {}, {}", id, playlistDTO);
        if (playlistDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id"+ ENTITY_NAME + "idnull");
        }
        if (!Objects.equals(id, playlistDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID"+ ENTITY_NAME + "idinvalid");
        }

        if (!playlistRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found"+ ENTITY_NAME + "idnotfound");
        }

        PlaylistDTO result = playlistService.update(playlistDTO);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylist(@PathVariable("id") Long id) {
        log.debug("REST request to get Playlist : {}", id);
        Optional<PlaylistDTO> playlistDTO = playlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playlistDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable("id") Long id) {
        log.debug("REST request to delete Playlist : {}", id);
        playlistService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
