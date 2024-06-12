package com.exameple.mytube.video;

import com.exameple.mytube.video.domain.Video;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VideoDto{

    private Long VideoId;

    private String  VideoUrl;



    private String thumbnailUrl;

    private String title;

    private String filename;

    private String description;

    private Boolean isTemp;

    private Boolean isPublic;

    private Integer views;

    private Integer likeCounts;

    private Integer userid;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;

public VideoDto(Video video){
    VideoId = video.getId();
    VideoUrl= video.getVideoUrl();
    thumbnailUrl = video.getThumbnailUrl();
    description = video.getDescription();
    title = video.getTitle();
    filename = video.getFilename();
    isTemp = video.getIsTemp();
    isPublic = video.getIsPublic();
    views = video.getViews();
    likeCounts = video.getLikeCounts();
    userid =(int)video.getUser().getId();
    regDate = video.getRegDate();
    updateDate = video.getUpdateDate();
}
}
