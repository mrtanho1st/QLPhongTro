package com.minhtan.qlptbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class SaleOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SaleOffId")
    private Integer saleOffId;

    @Column(name = "RoomId", nullable = false)
    private Integer roomId;

    @OneToOne()
}
