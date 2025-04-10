    package com.smartict.ProjectPoll.entity;

    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Table(name = "usr")
    @Data
    public class Usr {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false, unique = true)
        private String username;

        @Column
        private String name;

        @Column
        private String lastName;

        @Column
        private String password;

        @ManyToOne
        @JoinColumn(name = "FKroleID", nullable = false)
        private Roles role;
    }