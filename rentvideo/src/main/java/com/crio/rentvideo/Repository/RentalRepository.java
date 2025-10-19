package com.crio.rentvideo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crio.rentvideo.Entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long>{
    public long countByUserIdAndReturnAtIsNull(Long userId);
    public List<Rental> findByUserIdAndVideoIdAndReturnAtIsNull(Long userId, Long videoId);
    public List<Rental> findByUserIdAndReturnAtIsNull(Long userId);
}
