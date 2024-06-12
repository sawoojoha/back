package com.exameple.mytube.video.service;

import com.exameple.mytube.user.data.UserRepository;
import com.exameple.mytube.user.domain.User;
import com.exameple.mytube.video.VideoDto;
import com.exameple.mytube.video.dao.VideoRepository;
import com.exameple.mytube.video.domain.Video;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.api.MediaInfo;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.containers.mp4.demuxer.MP4DemuxerTrack;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.jcodec.common.model.Picture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class VideoService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final String keyPath = "src/main/resources/cogent-coyote-417714-9122d63aa011.json";//gsc

    public void uploadFiles(Integer userId, List<MultipartFile> files) throws IllegalStateException, IOException, JCodecException {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("File list is empty");
        }

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Map<String, String>> fileList = new ArrayList<>();

        // Google Cloud Storage 클라이언트 초기화
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath)))
                .setProjectId("cogent-coyote-417714")
                .build()
                .getService();

        for (int i = 0; i < files.size(); i++) {
            String orginalFilename = files.get(i).getOriginalFilename();
            String ext = orginalFilename.substring(orginalFilename.lastIndexOf("."));
            String title = orginalFilename.substring(0, orginalFilename.lastIndexOf("."));
            String changedFilename = UUID.randomUUID().toString() + ext; // 공백 제거
            String thumbnailFilename = changedFilename.substring(0, changedFilename.lastIndexOf(".")) + "png";

            // 비디오 파일 업로드
            try (InputStream inputStream = files.get(i).getInputStream()) {
                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, changedFilename).build();
                Blob blob = storage.create(blobInfo, inputStream);
            }
            // 썸네일 생성 및 업로드
            File tempFile = File.createTempFile("temp", ".mp4");
            files.get(i).transferTo(tempFile);
            Picture picture = FrameGrab.getFrameFromFile(tempFile, 0);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] thumbnailBytes = byteArrayOutputStream.toByteArray();

            // 썸네일 파일 업로드
            BlobInfo thumbnailBlobInfo = BlobInfo.newBuilder(bucketName, thumbnailFilename).build();
            Blob thumbnailBlob = storage.create(thumbnailBlobInfo, thumbnailBytes);

            // 파일 정보를 리스트에 추가
            Map<String, String> map = new HashMap<>();
            map.put("originalFilename", orginalFilename);
            map.put("changedFilename", changedFilename);
            map.put("thumbnail", thumbnailFilename);
            fileList.add(map);

            // 비디오 엔터티 생성 및 저장
            Video video = Video.builder()
                    .user(user)
                    .videoUrl("https://storage.googleapis.com/" + bucketName + "/" + changedFilename)
                    .title(title)
                    .filename(orginalFilename)
                    .thumbnailUrl("https://storage.googleapis.com/" + bucketName + "/" + thumbnailFilename)
                    .build();

            videoRepository.save(video);

            // 임시 파일 삭제
            tempFile.delete();
        }
    }

    public List<VideoDto> getVideos(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        List<Video> videos = videoRepository.findByUser(user);
        List<VideoDto> collect = videos.stream()
                .map(video -> {
                    VideoDto Dto = new VideoDto(video);
                    return Dto;
                })
                .collect(Collectors.toList());
        return collect;
    }


    public void updateVideo(Video video, int videoId) {
        Optional<Video> optionalVideo = videoRepository.findById(Long.valueOf(videoId));
        if (optionalVideo.isPresent()) {
            Video videoToUpdate = optionalVideo.get();
            videoToUpdate.setDescription(video.getDescription());
            videoToUpdate.setTitle(video.getTitle());

            if (video.getIsPublic() != null) {
                videoToUpdate.setIsPublic(video.getIsPublic());
            }
            if (video.getIsTemp() != null) {
                videoToUpdate.setIsTemp(video.getIsTemp());
            }
            videoRepository.save(videoToUpdate);
        }
    }

    public VideoDto getVideo(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(Long.valueOf(videoId));
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            VideoDto videoDto = new VideoDto(video);
            return videoDto;
        }
        return null;
    }

    public void updateVideoThumbnail(int videoId, MultipartFile newThumbnailFile) throws IOException {
        // 원래 비디오 엔티티 찾아오기
        Optional<Video> optionalVideo = videoRepository.findById((long) videoId);
        if (!optionalVideo.isPresent()) {
            throw new IllegalArgumentException("Video not found");
        }
        Video video = optionalVideo.get();

        // 원래 썸네일 삭제
        String originalThumbnailUrl = video.getThumbnailUrl();
        if (originalThumbnailUrl != null && !originalThumbnailUrl.isEmpty()) {
            // 이 부분은 삭제하지 않습니다.
            // 삭제가 필요하다면 필요한 로직을 추가하세요.
        }

        // 새로운 썸네일을 업로드하고 URL 업데이트
        String newThumbnailFilename = UUID.randomUUID().toString() + ".png";

        // Google Cloud Storage 클라이언트 초기화
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath)))
                .setProjectId("cogent-coyote-417714")
                .build()
                .getService();

        try (InputStream inputStream = newThumbnailFile.getInputStream()) {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, newThumbnailFilename).build();
            Blob blob = storage.create(blobInfo, inputStream);
        }

        // 비디오 엔터티에 업데이트된 썸네일 URL 설정
        String newThumbnailUrl = "https://storage.googleapis.com/" + bucketName + "/" + newThumbnailFilename;
        video.setThumbnailUrl(newThumbnailUrl);

        videoRepository.save(video);
    }
}