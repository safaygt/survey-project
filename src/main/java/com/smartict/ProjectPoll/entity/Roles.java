package com.smartict.ProjectPoll.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rols")
@Data
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) // Enum tipini veritabanında String olarak sakla
    @Column(nullable = false, unique = true)
    private EnumRole roleText;
}