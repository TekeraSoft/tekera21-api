package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ThemeDto;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateThemeRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ThemeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeDto> findAll() {
        return themeRepository.findAll().stream().map(ThemeDto::toDto).collect(Collectors.toList());
    }

    public ApiResponse<?> updateTheme(UpdateThemeRequest req, List<String> images) {

    }
}
