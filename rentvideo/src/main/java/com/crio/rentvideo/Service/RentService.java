package com.crio.rentvideo.Service;

import java.util.List;

import com.crio.rentvideo.Dto.RentalDTO;

public interface RentService {
    public RentalDTO rentVideo(String email, long videoID);
    public RentalDTO returnVideo(String email, long videoId);
    public List<RentalDTO> getMyActiveRentals(String email);
}
