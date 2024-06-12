package com.exameple.mytube.video.controller;



import com.exameple.mytube.fileSystem.FileSystemService;
import com.exameple.mytube.video.VideoDto;
import com.exameple.mytube.video.domain.Video;
import com.exameple.mytube.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {

    private final FileSystemService fileSystemService;
    private  final VideoService videoService;



    @GetMapping("")
    public List<VideoDto> getVideos(@RequestParam("userId")Integer userId){
        System.out.println(userId);
        return videoService.getVideos(userId);
    }
    @GetMapping("/{videoId}")
    public VideoDto getVideo(@PathVariable("videoId")Integer videoId){
        System.out.println("videoId:" + videoId);

        return videoService.getVideo(videoId);
    }
    @PostMapping("")
    public List<VideoDto> uploadVideos(@RequestParam("files") List<MultipartFile> files, @RequestParam("userId") Integer userId) throws IOException, IllegalStateException, JCodecException {


        videoService.uploadFiles(userId, files);

        return videoService.getVideos(userId);
    }

    @PatchMapping("/{videoId}")
    public void updateVideo(@PathVariable("videoId") int videoId , @RequestBody Video video){
        videoService.updateVideo(video, videoId);
        System.out.println(video);
        System.out.println(videoId);
    }
    @PatchMapping("{videoId}/newThumbnail")
    public void updateVideoThumbnail(@PathVariable("videoId") int videoId, @RequestParam("thumbnailFile") MultipartFile newThumbnailFile) throws IOException {
        videoService.updateVideoThumbnail(videoId, newThumbnailFile);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}

