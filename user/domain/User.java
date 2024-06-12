package com.exameple.mytube.user.domain;

import com.exameple.mytube.video.domain.Video;
import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;


    @Column(unique = true, nullable = false)
    private String password;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Video> videoList;
}