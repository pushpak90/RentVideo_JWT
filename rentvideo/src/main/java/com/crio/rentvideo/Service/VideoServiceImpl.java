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

    @Override
    public VideoDTO updateVideo(long id, VideoDTO videoDTO) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found by ID : " + id));
        video.setDirector(videoDTO.getDirector());
        video.setGenre(videoDTO.getGenre());
        video.setTitle(videoDTO.getTitle());

        return modelMapper.map(videoRepository.save(video), VideoDTO.class);
    }

    @Override
    public boolean deleteVideo(long id) {

        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
