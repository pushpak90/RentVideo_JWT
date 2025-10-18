package com.crio.rentvideo.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.rentvideo.Dto.RentalDTO;
import com.crio.rentvideo.Service.RentService;

@RestController
@RequestMapping("/api/rentals")
public class RentController {
    @Autowired
    private RentService rentService;

    @PostMapping("/{videoId}/rent")
    public ResponseEntity<RentalDTO> rentVideo(@PathVariable("videoId") long videoId, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.rentVideo(principal.getName(), videoId));
    }
}
