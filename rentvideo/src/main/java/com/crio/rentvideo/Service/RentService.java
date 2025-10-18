package com.crio.rentvideo.Service;

import com.crio.rentvideo.Dto.RentalDTO;

public interface RentService {
    public RentalDTO rentVideo(String email, long videoID);
}
