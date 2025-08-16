package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.model.entity.Role;
import com.tekerasoft.tekeramarketplace.repository.jparepository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(String roleName) {
        return  roleRepository.findByName(roleName);
    }
}
