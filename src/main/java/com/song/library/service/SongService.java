package com.song.library.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.library.domain.Song;
import com.song.library.dto.SongDTO;
import com.song.library.dto.SongMapper;
import com.song.library.repository.SongRepository;

@Service
@Transactional
public class SongService {

   private final Logger log = LoggerFactory.getLogger(SongService.class);

   private final SongRepository songRepository;

   private final SongMapper songMapper;

   public SongService(SongRepository songRepository, SongMapper songMapper) {
      this.songRepository = songRepository;
      this.songMapper = songMapper;
   }

   public SongDTO save(SongDTO songDTO) {
      log.debug("Request to save Song : {}", songDTO);
      Song song = songMapper.toEntity(songDTO);
      song = songRepository.save(song);
      return songMapper.toDto(song);
   }

   public SongDTO update(SongDTO songDTO) {
      log.debug("Request to update Song : {}", songDTO);
      Song song = songMapper.toEntity(songDTO);
      song = songRepository.save(song);
      return songMapper.toDto(song);
   }

   @Transactional(readOnly = true)
   public Page<SongDTO> findAll(Pageable pageable) {
      log.debug("Request to get all Songs");
      return songRepository.findAll(pageable).map(songMapper::toDto);
   }

   public Page<SongDTO> findAllWithEagerRelationships(Pageable pageable) {
      return songRepository.findAllWithEagerRelationships(pageable).map(songMapper::toDto);
   }

   @Transactional(readOnly = true)
   public Optional<SongDTO> findOne(Long id) {
      log.debug("Request to get Song : {}", id);
      return songRepository.findOneWithEagerRelationships(id).map(songMapper::toDto);
   }

   public void delete(Long id) {
      log.debug("Request to delete Song : {}", id);
      songRepository.deleteById(id);
   }
}
