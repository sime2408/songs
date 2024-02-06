package com.song.library.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.song.library.domain.Song;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class SongRepositoryWithBagRelationshipsImpl implements SongRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Song> fetchBagRelationships(Optional<Song> song) {
        return song.map(this::fetchPlaylists);
    }

    @Override
    public Page<Song> fetchBagRelationships(Page<Song> songs) {
        return new PageImpl<>(fetchBagRelationships(songs.getContent()), songs.getPageable(), songs.getTotalElements());
    }

    @Override
    public List<Song> fetchBagRelationships(List<Song> songs) {
        return Optional.of(songs).map(this::fetchPlaylists).orElse(Collections.emptyList());
    }

    Song fetchPlaylists(Song result) {
        return entityManager
            .createQuery("select song from Song song left join fetch song.playlists where song.id = :id", Song.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Song> fetchPlaylists(List<Song> songs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, songs.size()).forEach(index -> order.put(songs.get(index).getId(), index));
        List<Song> result = entityManager
            .createQuery("select song from Song song left join fetch song.playlists where song in :songs", Song.class)
            .setParameter("songs", songs)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
