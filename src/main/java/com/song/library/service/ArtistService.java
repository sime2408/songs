package com.song.library.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.library.domain.Artist;
import com.song.library.dto.ArtistDTO;
import com.song.library.dto.ArtistMapper;
import com.song.library.repository.ArtistRepository;

@Service
public class ArtistService {

   private final Logger log = LoggerFactory.getLogger(ArtistService.class);

   private final ArtistRepository artistRepository;
   private final ArtistMapper artistMapper;

   public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
      this.artistRepository = artistRepository;
      this.artistMapper = artistMapper;
   }

   public ArtistDTO save(ArtistDTO artistDTO) {
      log.debug("Request to save Artist : {}", artistDTO);
      Artist artist = artistMapper.toEntity(artistDTO);
      artist = artistRepository.save(artist);
      return artistMapper.toDto(artist);
   }

   public ArtistDTO update(ArtistDTO artistDTO) {
      log.debug("Request to update Artist : {}", artistDTO);
      Artist artist = artistMapper.toEntity(artistDTO);
      artist = artistRepository.save(artist);
      return artistMapper.toDto(artist);
   }

   public Optional<ArtistDTO> partialUpdate(ArtistDTO artistDTO) {
      log.debug("Request to partially update Artist : {}", artistDTO);

      return artistRepository
            .findById(artistDTO.getId())
            .map(existingArtist -> {
               artistMapper.partialUpdate(existingArtist, artistDTO);

               return existingArtist;
            })
            .map(artistRepository::save)
            .map(artistMapper::toDto);
   }

   @Transactional(readOnly = true)
   public Page<ArtistDTO> findAll(Pageable pageable) {
      log.debug("Request to get all Artists");
      return artistRepository.findAll(pageable).map(artistMapper::toDto);
   }

   @Transactional(readOnly = true)
   public Optional<ArtistDTO> findOne(Long id) {
      log.debug("Request to get Artist : {}", id);
      return artistRepository.findById(id).map(artistMapper::toDto);
   }

   public void delete(Long id) {
      log.debug("Request to delete Artist : {}", id);
      artistRepository.deleteById(id);
   }
}
