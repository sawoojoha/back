package com.exameple.mytube.video.domain;

import com.exameple.mytube.comment.domain.Comment;
import com.exameple.mytube.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@DynamicInsert
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 255)
    private String videoUrl;

    @Column(length = 255)
    private String thumbnailUrl;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String filename;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "tinyint(1) default(1)")
    private Boolean isTemp = true;

    @Column(columnDefinition = "tinyint(1) default(0)")
    private Boolean isPublic = false;

    @Column(columnDefinition = "integer default 0" )
    private Integer views = 0;

    @Column(columnDefinition = "integer default 0" )
    private Integer likeCounts = 0;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private User user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDate;

    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now();
    }

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;


    @OneToMany(mappedBy = "video", cascade = CascadeType.REMOVE)
    private List<Comment> comments;


}
