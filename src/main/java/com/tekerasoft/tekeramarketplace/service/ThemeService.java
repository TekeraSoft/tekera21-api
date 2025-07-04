package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ThemeDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateThemeRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateThemeRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.Theme;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ThemeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ApiResponse<?> createTheme(List<CreateThemeRequest> req) {
        try {
            List<Theme> themes = new ArrayList<>();
            req.forEach(treq -> {
                Theme theme = new Theme();
                theme.setName(treq.getName());
                String themeImagePath = fileService.folderFileUpload(treq.getImage(), "theme-images");
                theme.setImage(themeImagePath);
                themes.add(theme);
            });
            themeRepository.saveAll(themes);
            return new ApiResponse<>("Themes Created", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<?> updateTheme(List<UpdateThemeRequest> req) {
        try {
            req.forEach(treq -> {
                Theme theme = themeRepository.findById(UUID.fromString(treq.getId()))
                        .orElseThrow(() -> new NotFoundException("Theme not found"));
                theme.setName(treq.getName());
                if(!treq.getImage().isEmpty()) {
                    fileService.deleteInFolderFile(theme.getImage());
                    String imagePath = fileService.folderFileUpload(treq.getImage(), "theme-images");
                    theme.setImage(imagePath);
                }
                themeRepository.save(theme);
            });
            return  new ApiResponse<>("Themes Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<?> deleteTheme(String id) {
        Theme theme = themeRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotFoundException("Theme not found"));
        try {
                fileService.deleteInFolderFile(theme.getImage());
                themeRepository.deleteById(UUID.fromString(id));
                return new ApiResponse<>("Theme Deleted", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ThemeDto> getAllTheme() {
        return themeRepository.findAll().stream().map(ThemeDto::toDto).collect(Collectors.toList());
    }

}
