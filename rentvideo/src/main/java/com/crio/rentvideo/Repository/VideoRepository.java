package com.crio.rentvideo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.rentvideo.Entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{
    List<Video> findByAvailableTrue();
}
