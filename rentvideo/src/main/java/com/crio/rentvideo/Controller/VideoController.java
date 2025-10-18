package com.crio.rentvideo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping
    public ResponseEntity<VideoDTO> addVideo(@RequestBody VideoDTO videoDTO) {
        return ResponseEntity.ok(videoService.addVideo(videoDTO));
    }

    @GetMapping
    public ResponseEntity<List<VideoDTO>> getAllAvailableVideos() {
        return ResponseEntity.ok(videoService.getAllAvailableVideos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDTO> updateVideo(@PathVariable("id") long id ,@RequestBody VideoDTO videoDTO){
        return ResponseEntity.ok(videoService.updateVideo(id, videoDTO));
    }

    @DeleteMapping("/{id}")
    public String deleteVideo(@PathVariable("id") long id){
        if(videoService.deleteVideo(id)){
            return "Video Deleted";
        }
        return "Video not found at ID : " + id;
    }
}
