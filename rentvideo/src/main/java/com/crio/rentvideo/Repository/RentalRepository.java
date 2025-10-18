package com.crio.rentvideo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.rentvideo.Entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long>{
    long countByUserIdAndReturnAtIsNull(Long userId);
    
}
