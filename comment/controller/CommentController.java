package com.exameple.mytube.comment.controller;

import com.exameple.mytube.comment.domain.Comment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @PostMapping("/{videoId}")
    private void createComment(@PathVariable Long videoId, @RequestBody Comment comment ){
System.out.println(comment.getText());
System.out.println(videoId);
    }
}
