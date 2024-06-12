package com.exameple.mytube.video.dao;

import com.exameple.mytube.user.domain.User;
import com.exameple.mytube.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video , Long> {
    List<Video> findByUser(User user);
}
