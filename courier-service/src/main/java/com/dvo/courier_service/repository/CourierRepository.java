package com.dvo.courier_service.repository;

import com.dvo.courier_service.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    boolean existsByUserId(Long userId);
}
