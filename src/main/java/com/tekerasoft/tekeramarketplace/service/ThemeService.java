package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ThemeDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateThemeRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateThemeRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.exception.ThemeException;
import com.tekerasoft.tekeramarketplace.model.entity.Theme;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ThemeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final FileService fileService;

    public ThemeService(ThemeRepository themeRepository, FileService fileService) {
        this.themeRepository = themeRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> createTheme(CreateThemeRequest req) {

            if(themeRepository.existsByName(req.getName())) {
                throw new ThemeException("Theme already exist");
            }
                Theme theme = new Theme();
                theme.setName(req.getName());
                String themeImagePath = fileService.folderFileUpload(req.getImage(), "theme-images");
                theme.setImage(themeImagePath);
            themeRepository.save(theme);
            return new ApiResponse<>("Themes Created", HttpStatus.CREATED.value());
    }

    public ApiResponse<?> updateTheme(UpdateThemeRequest req) {
        try {
                Theme theme = themeRepository.findById(UUID.fromString(req.getId()))
                        .orElseThrow(() -> new NotFoundException("Theme not found"));
                theme.setName(req.getName());
                if(!req.getImage().isEmpty()) {
                    fileService.deleteInFolderFile(theme.getImage());
                    String imagePath = fileService.folderFileUpload(req.getImage(), "theme-images");
                    theme.setImage(imagePath);
                }
                themeRepository.save(theme);
            return  new ApiResponse<>("Themes Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<?> deleteTheme(String id) {
        Theme theme = themeRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotFoundException("Theme not found"));

                fileService.deleteInFolderFile(theme.getImage());
                themeRepository.deleteById(UUID.fromString(id));
                return new ApiResponse<>("Theme Deleted", HttpStatus.OK.value());
    }

    public List<ThemeDto> getAllTheme() {
        return themeRepository.findAll().stream().map(ThemeDto::toDto).collect(Collectors.toList());
    }

}
