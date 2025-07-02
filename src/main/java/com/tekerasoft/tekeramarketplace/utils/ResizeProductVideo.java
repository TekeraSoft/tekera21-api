package com.tekerasoft.tekeramarketplace.utils;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ProductRepository;
import com.tekerasoft.tekeramarketplace.service.FileService;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class ResizeProductVideo {

    private final FileService fileService;
    private final ProductRepository productRepository;

    public ResizeProductVideo(FileService fileService, ProductRepository productRepository) {
        this.fileService = fileService;
        this.productRepository = productRepository;
    }

    public String resizeProductVideo(MultipartFile video, String companyName) throws IOException {

        /* 0) Kaynağı seek‑edilebilir dosyaya yaz */
        String ext = Optional.ofNullable(video.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.')).toLowerCase())
                .orElse(".mp4");

        Path raw  = Files.createTempFile("raw-",  ext);   // .mov ya da .mp4
        video.transferTo(raw.toFile());                   // HEAP'e kopya yok!

        Path temp = Files.createTempFile("resized-", ".mp4");

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(raw.toFile());
             FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(temp.toString(), 1280, 720)) {

            grabber.setFormat(ext.equals(".mov") ? "mov" : "mp4");
            grabber.start();                                   // ← Hata artık yok

            recorder.setFormat("mp4");
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);   // sesi koru
            recorder.setAudioBitrate(grabber.getAudioBitrate());
            recorder.setAudioChannels(grabber.getAudioChannels());
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoOption("crf", "28");
            recorder.setVideoOption("preset", "slow");
            recorder.start();

            Frame f;
            while ((f = grabber.grabFrame()) != null) recorder.record(f);

            recorder.stop();
            grabber.stop();
        }

        /* 3) MinIO'ya yolla */
        String url;
        try (InputStream is = Files.newInputStream(temp)) {
            url = fileService.productVideoUpload(is,
                    Files.size(temp),
                    "video/mp4",
                    ".mp4",
                    companyName);
        } finally {
            Files.deleteIfExists(raw);
            Files.deleteIfExists(temp);
        }
        return url;
    }
}
