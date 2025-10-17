package com.crio.rentvideo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crio.rentvideo.Dto.VideoDTO;
import com.crio.rentvideo.Entity.Video;
import com.crio.rentvideo.Repository.VideoRepository;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public VideoDTO addVideo(VideoDTO videoDTO) {
        Video video = modelMapper.map(videoDTO, Video.class);
        Video saved = videoRepository.save(video);
        return modelMapper.map(saved, VideoDTO.class);
    }

    @Override
    public List<VideoDTO> getAllAvailableVideos() {
        List<Video> videos = videoRepository.findByAvailableTrue();
        return videos.stream()
                .map(v -> modelMapper.map(v, VideoDTO.class)).collect(Collectors.toList());
    }

}
