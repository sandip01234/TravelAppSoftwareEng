package com.example.Travel.Application.Repository;


import com.example.Travel.Application.Entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByUserId(Long userId);


    // Additional query methods can be defined here if needed
}
