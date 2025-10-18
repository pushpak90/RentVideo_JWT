package com.crio.rentvideo.Dto;

import java.time.LocalDateTime;

import com.crio.rentvideo.Entity.User;
import com.crio.rentvideo.Entity.Video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private long id;
    private User user;
    private Video video;
    private LocalDateTime rentAt;
    private LocalDateTime returnAt;
}
