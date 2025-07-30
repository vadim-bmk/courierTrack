package com.dvo.courier_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "couriers")
@Table(name = "couriers")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "user_id")
    private Long userId;

    private String region;

    @Column(name = "current_status")
    @Enumerated(EnumType.STRING)
    private CourierStatus currentStatus;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL)
    private List<DeliveryAssignment> assignments;
}
