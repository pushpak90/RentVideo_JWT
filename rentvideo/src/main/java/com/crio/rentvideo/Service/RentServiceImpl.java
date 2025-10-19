package com.crio.rentvideo.Service;

import java.time.LocalDateTime;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crio.rentvideo.Dto.RentalDTO;
import com.crio.rentvideo.Entity.Rental;
import com.crio.rentvideo.Entity.User;
import com.crio.rentvideo.Entity.Video;
import com.crio.rentvideo.Repository.RentalRepository;
import com.crio.rentvideo.Repository.UserRepository;
import com.crio.rentvideo.Repository.VideoRepository;

import jakarta.transaction.Transactional;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public RentalDTO rentVideo(String email, long videoID) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NameNotFoundException("User Not Found."));
            if (rentalRepository.countByUserIdAndReturnAtIsNull(user.getId()) >= 2) {
                throw new RuntimeException("Max 2 active rentals allowed");
            }

            Video video = videoRepository.findById(videoID)
                    .orElseThrow(() -> new RuntimeException("Video not found by ID : " + videoID));

            video.setAvailable(false);
            videoRepository.save(video);

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setVideo(video);
            rental.setRentAt(LocalDateTime.now());
            rental.setReturnAt(null);

            return modelMapper.map(rentalRepository.save(rental), RentalDTO.class);

        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    @Transactional
    public RentalDTO returnVideo(String email, long videoId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found By Email : " + email));

        List<Rental> rentals = rentalRepository.findByUserIdAndVideoIdAndReturnAtIsNull(user.getId(), videoId);

        if (rentals.isEmpty()) {
            throw new RuntimeException("No active rental found for this user and video");
        }
        Rental rental = rentals.get(0);
        rental.setReturnAt(LocalDateTime.now());

        videoRepository.findById(videoId).ifPresent(v -> {
            v.setAvailable(true);
            videoRepository.save(v);
        });

        return modelMapper.map(rentalRepository.save(rental), RentalDTO.class);
    }

    @Override
    public List<RentalDTO> getMyActiveRentals(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        return rentalRepository.findByUserIdAndReturnAtIsNull(user.getId())
        .stream()
        .map(r -> modelMapper.map(r, RentalDTO.class)).toList();
    }

}
