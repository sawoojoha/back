package com.exameple.mytube.comment.domain;

import com.exameple.mytube.video.domain.Video;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(columnDefinition = "integer default 0")
    private Long likes;

    @ManyToOne
    private Video video;
}
