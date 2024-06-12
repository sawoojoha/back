package com.exameple.mytube.video.service;

import com.exameple.mytube.video.VideoDto;
import com.exameple.mytube.video.dao.VideoRepository;
import com.exameple.mytube.video.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService2 {
    @Autowired
    private VideoRepository videoRepository;

    public VideoDto increaseViewCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(Long.valueOf(videoId));
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setViews(video.getViews() + 1); // 조회수 1 증가
            videoRepository.save(video); // 변경사항 저장

            return new VideoDto(video); // 변경된 비디오 정보를 VideoDto로 매핑하여 반환
        }
        return null;
    }

    public VideoDto increaseLikeCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(Long.valueOf(videoId));
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setLikeCounts(video.getLikeCounts() + 1); // 조회수 1 증가
            videoRepository.save(video); // 변경사항 저장

            return new VideoDto(video); // 변경된 비디오 정보를 VideoDto로 매핑하여 반환
        }
        return null;
    }
}
