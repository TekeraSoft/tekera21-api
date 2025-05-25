package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.document.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.document.TargetPicture;
import com.tekerasoft.tekeramarketplace.repository.nosql.DigitalFashionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DigitalFashionService {
    private final DigitalFashionRepository digitalFashionRepository;
    private final FileService fileService;

    public DigitalFashionService(DigitalFashionRepository targetPictureRepository,
                                 DigitalFashionRepository digitalFashionRepository,
                                 FileService fileService) {
        this.digitalFashionRepository = digitalFashionRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> createTargetPicture(CreateTargetPictureRequest req) {
        try {
            String filePath = fileService.targetPicUpload(req.getPicture());
            String mindPath = fileService.targetPicUpload(req.getMind());
            TargetPicture targetPicture = new TargetPicture();
            targetPicture.setTargetPic(filePath);
            targetPicture.setMindPath(mindPath);
            return new ApiResponse<>("File Saved", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<TargetPictureDto> getAllTargetPictures(Pageable pageable) {
        return digitalFashionRepository.findAll(pageable).map(TargetPictureDto::toDto);
    }

    public ApiResponse<?> deleteTargetPicture(String id) {
        try {
            digitalFashionRepository.deleteById(id);
            return new ApiResponse<>("TargetPicture deleted", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
