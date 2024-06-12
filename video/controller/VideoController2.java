package com.exameple.mytube.video.controller;

import com.exameple.mytube.video.VideoDto;
import com.exameple.mytube.video.service.VideoService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/setting")
@RequiredArgsConstructor
public class VideoController2 {
    private final VideoService2 videoService2;

    @PostMapping("{videoId}") // @PostMapping으로 수정
    public VideoDto increaseViewCount(@PathVariable("videoId") Integer videoId) {
        System.out.println("videoId:" + videoId);
        return videoService2.increaseViewCount(videoId);
    }
    @PostMapping("{videoId}/likes") // @PostMapping으로 수정
    public VideoDto increaseLikeCount(@PathVariable("videoId") Integer videoId) {
        System.out.println("videoId:" + videoId);
        return videoService2.increaseLikeCount(videoId);
    }
}
