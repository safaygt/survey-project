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

        @Column(nullable = false, unique = true)
        private String roleText;
    }