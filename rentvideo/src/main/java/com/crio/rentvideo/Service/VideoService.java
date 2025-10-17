package com.crio.rentvideo.Service;

import java.util.List;

import com.crio.rentvideo.Dto.VideoDTO;

public interface VideoService {
    public VideoDTO addVideo(VideoDTO videoDTO);
    public List<VideoDTO> getAllAvailableVideos();
}
