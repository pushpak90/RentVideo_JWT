package com.crio.rentvideo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.rentvideo.Dto.VideoDTO;
import com.crio.rentvideo.Service.VideoService;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<VideoDTO> addVideo(@RequestBody VideoDTO videoDTO) {
        return ResponseEntity.ok(videoService.addVideo(videoDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping ResponseEntity<List<VideoDTO>> getAllAvailableVideos(){
        return ResponseEntity.ok(videoService.getAllAvailableVideos());
    }
}
