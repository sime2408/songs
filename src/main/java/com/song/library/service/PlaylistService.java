package com.song.library.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.library.domain.Playlist;
import com.song.library.dto.PlaylistDTO;
import com.song.library.dto.PlaylistMapper;
import com.song.library.repository.PlaylistRepository;

@Service
@Transactional
public class PlaylistService {

   private final Logger log = LoggerFactory.getLogger(PlaylistService.class);

   private final PlaylistRepository playlistRepository;

   private final PlaylistMapper playlistMapper;

   public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper) {
      this.playlistRepository = playlistRepository;
      this.playlistMapper = playlistMapper;
   }

   public PlaylistDTO save(PlaylistDTO playlistDTO) {
      log.debug("Request to save Playlist : {}", playlistDTO);
      Playlist playlist = playlistMapper.toEntity(playlistDTO);
      playlist = playlistRepository.save(playlist);
      return playlistMapper.toDto(playlist);
   }

   public PlaylistDTO update(PlaylistDTO playlistDTO) {
      log.debug("Request to update Playlist : {}", playlistDTO);
      Playlist playlist = playlistMapper.toEntity(playlistDTO);
      playlist = playlistRepository.save(playlist);
      return playlistMapper.toDto(playlist);
   }

   public Optional<PlaylistDTO> partialUpdate(PlaylistDTO playlistDTO) {
      log.debug("Request to partially update Playlist : {}", playlistDTO);

      return playlistRepository
            .findById(playlistDTO.getId())
            .map(existingPlaylist -> {
               playlistMapper.partialUpdate(existingPlaylist, playlistDTO);

               return existingPlaylist;
            })
            .map(playlistRepository::save)
            .map(playlistMapper::toDto);
   }

   @Transactional(readOnly = true)
   public Page<PlaylistDTO> findAll(Pageable pageable) {
      log.debug("Request to get all Playlists");
      return playlistRepository.findAll(pageable).map(playlistMapper::toDto);
   }

   @Transactional(readOnly = true)
   public Optional<PlaylistDTO> findOne(Long id) {
      log.debug("Request to get Playlist : {}", id);
      return playlistRepository.findById(id).map(playlistMapper::toDto);
   }

   public void delete(Long id) {
      log.debug("Request to delete Playlist : {}", id);
      playlistRepository.deleteById(id);
   }

}
