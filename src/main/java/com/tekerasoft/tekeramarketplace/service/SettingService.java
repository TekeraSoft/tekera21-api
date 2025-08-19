package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.document.Setting;
import com.tekerasoft.tekeramarketplace.repository.mongorepository.SettingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    private final SettingRepository settingRepository;
    private Setting currentSettings;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @PostConstruct
    public void loadSettings() {
        this.currentSettings = settingRepository.count() != 0 ? settingRepository.findAll().get(0) : null;
    }

    public Setting getSettings() {
        return this.currentSettings;
    }

    // Ayarları güncellemek için bir metod
    public ApiResponse<Setting> updateSettings(Setting newSettings) {

        Setting setting =  settingRepository.save(newSettings);
        this.currentSettings = setting;

        return new ApiResponse<>("Ayarlar güncellendi",HttpStatus.OK.value() ,setting);
    }
}
