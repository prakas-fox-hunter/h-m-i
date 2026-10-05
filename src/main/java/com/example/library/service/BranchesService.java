package com.example.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.library.dto.ActiveAndNonDeleteDataDto;
import com.example.library.dto.BranchesDto;
import com.example.library.entity.Branches;
import com.example.library.repository.BranchesRepository;
import com.example.library.repository.ProvincesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import com.example.library.entity.Provinces;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchesService {
    private final BranchesRepository branchRepo;
    private final ProvincesRepository provinceRepo;

    public BranchesDto updateBranch(Long id, BranchesDto branchDto) {
        // Cari branch berdasarkan ID
        Branches branch = branchRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));

        // Cari provinsi berdasarkan nama
        Provinces province = provinceRepo.findByName(branchDto.getProvinceName()).orElseThrow(
                () -> new EntityNotFoundException("Province not found with name: " + branchDto.getProvinceName()));
        if (province == null) {
            log.error("Province not found with name: {}", branchDto.getProvinceName());
            throw new EntityNotFoundException("Province not found with name: " + branchDto.getProvinceName());
        }

        // Update branch
        branch.setName(branchDto.getName());
        branch.setProvince(provinceRepo.findByName(branchDto.getProvinceName()).get());
        branch.setActive(branchDto.isActive());
        branch.setDeleted(branchDto.isDeleted());

        branchRepo.save(branch);
        log.info("Branch updated successfully: id={}, name={}, province={}",
                branch.getId(), branch.getName(), province.getName());

        // Mapping kembali ke DTO
        BranchesDto result = new BranchesDto();
        result.setName(branch.getName());
        result.setProvinceName(branch.getProvince().getName());
        result.setActive(branch.isActive());
        result.setDeleted(branch.isDeleted());

        return result;
    }

    public String deleteBranch(Long id) {
        Branches b = branchRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
        b.setDeleted(true); 
        branchRepo.save(b);
        branchRepo.delete(b);
        return "Branch with id " + b.getId() + " and name " + b.getName() + " deleted successfully";
    }

}
