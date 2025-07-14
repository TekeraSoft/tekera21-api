package com.tekerasoft.tekeramarketplace.utils;

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
import java.nio.file.StandardCopyOption;
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

        String ext = Optional.ofNullable(video.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.')).toLowerCase())
                .orElse(".mp4");

        Path raw  = Files.createTempFile("raw-",  ext);
        try (InputStream videoStream = video.getInputStream()) {
            Files.copy(videoStream, raw, StandardCopyOption.REPLACE_EXISTING);
        }

        Path temp = Files.createTempFile("resized-", ".mp4");

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(raw.toFile())) {
            // !!! BURADA DEĞİŞİKLİK YAPIYORUZ !!!
            // FFmpeg'in otomatik rotasyonunu engellemek için input option ekliyoruz.
            // Bazı JavaCV sürümlerinde setOption("noautorotate", "1") işe yarayabilir.
            // Eğer bu çalışmazsa, grabber.setVideoOption("noautorotate", "1") denenebilir.
            // Ya da doğrudan "noautorotate" olarak eklemek gerekir.
            grabber.setOption("noautorotate", "1"); // Bu satır, otomatik rotasyonu engellemeye çalışır.

            grabber.start();

            int width = grabber.getImageWidth();
            int height = grabber.getImageHeight();

            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(temp.toString(), width, height, grabber.getAudioChannels())) {

                recorder.setFormat("mp4");
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                int audioChannels = grabber.getAudioChannels() > 0 ? grabber.getAudioChannels() : 1; // default 1 kanal
                // Ses codec'ini grabber'dan alarak ayarlayın
                recorder.setAudioCodec(grabber.getAudioCodec());
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                recorder.setAudioChannels(audioChannels);
                recorder.setFrameRate(grabber.getFrameRate());

                // CRF değerini 28 yaparak kaliteyi düşürüp boyut küçültme
                recorder.setVideoOption("crf", "28");
                recorder.setVideoOption("preset", "slow");

                recorder.start();

                Frame frame;
                while ((frame = grabber.grabFrame()) != null) {
                    recorder.record(frame);
                }

                recorder.stop();
            }

            grabber.stop();
        }

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
