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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.song.library.dto.ArtistDTO;
import com.song.library.repository.ArtistRepository;
import com.song.library.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
public class ArtistResource {

    private final Logger log = LoggerFactory.getLogger(ArtistResource.class);

    private static final String ENTITY_NAME = "artist";

    private final ArtistService artistService;

    private final ArtistRepository artistRepository;

    public ArtistResource(ArtistService artistService, ArtistRepository artistRepository) {
        this.artistService = artistService;
        this.artistRepository = artistRepository;
    }

    @PostMapping("")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) throws URISyntaxException {
        log.debug("REST request to save Artist : {}", artistDTO);
        if (artistDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new artist cannot already have an ID" + ENTITY_NAME + "idexists");
        }
        ArtistDTO result = artistService.save(artistDTO);
        return ResponseEntity
            .created(new URI("/api/artists/" + result.getId()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArtistDTO artistDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Artist : {}, {}", id, artistDTO);
        if (artistDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id" + ENTITY_NAME + "idnull");
        }
        if (!Objects.equals(id, artistDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID" + ENTITY_NAME + "idinvalid");
        }

        if (!artistRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found" + ENTITY_NAME + "idnotfound");
        }

        ArtistDTO result = artistService.update(artistDTO);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArtistDTO> partialUpdateArtist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArtistDTO artistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Artist partially : {}, {}", id, artistDTO);
        if (artistDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id" + ENTITY_NAME + "idnull");
        }
        if (!Objects.equals(id, artistDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID" + ENTITY_NAME + "idinvalid");
        }

        if (!artistRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found" + ENTITY_NAME + "idnotfound");
        }

        Optional<ArtistDTO> result = artistService.partialUpdate(artistDTO);

        ArtistDTO resultDTO = result.orElseThrow(() ->
              new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with ID: " + artistDTO.getId())
        );

        return ResponseEntity.ok().body(resultDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable("id") Long id) {
        log.debug("REST request to get Artist : {}", id);
        Optional<ArtistDTO> artistDTO = artistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artistDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable("id") Long id) {
        log.debug("REST request to delete Artist : {}", id);
        artistService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
