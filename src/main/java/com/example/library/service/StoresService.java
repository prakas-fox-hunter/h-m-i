package com.example.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.library.repository.BranchesRepository;
import com.example.library.repository.StoresRepository;

import lombok.RequiredArgsConstructor;

import com.example.library.dto.ActiveAndNonDeleteDataDto;
import com.example.library.dto.ProvinceByStoreDto;
import com.example.library.dto.StoresDto;
import com.example.library.dto.WhiteListedStoreDto;
import com.example.library.entity.Stores;

@Service
@RequiredArgsConstructor
public class StoresService {
    private final StoresRepository storeRepo;
    private final BranchesRepository branchesRepository;

    public List<Stores> searchByProvinceName(String province) {
        return storeRepo.findActiveByProvinceNameOrWhitelisted(province);
    }

    public List<ProvinceByStoreDto> searchStoresByProvinces(String province, String store) {
        return storeRepo.findByBranch_Province_NameContainingIgnoreCaseAndNameContainingIgnoreCase(province, store);
    }

    public Page<ActiveAndNonDeleteDataDto> findByActiveTrueAndDeletedFalse(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return storeRepo.findByActiveTrueAndDeletedFalse(pageable);
    }

    public List<WhiteListedStoreDto> findByWhitelisted() {
        return storeRepo.findByWhitelisted();
    }

    public String deleteStore(Long id) {
        Stores store = storeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + id));
        store.setDeleted(true);
        storeRepo.save(store);
        storeRepo.delete(store);
        return "Store with id: " + id + " has been marked as deleted.";
    }

    public StoresDto updateStore(Long id, StoresDto storeDto) {
        Stores store = storeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + id));

        store.setName(storeDto.getName());
        store.setActive(storeDto.isActive());
        store.setDeleted(storeDto.isDeleted());
        store.setWhitelisted(storeDto.isWhitelisted());
        store.setBranch(storeDto.getBranchName() != null ? branchesRepository.findByName(storeDto.getBranchName()).get() : null ); // Assuming branch update is handled separately
        storeRepo.save(store);

        StoresDto result = new StoresDto();
        result.setName(store.getName());
        result.setActive(store.isActive());
        result.setDeleted(store.isDeleted());
        result.setWhitelisted(store.isWhitelisted());
        result.setBranchName(store.getBranch() != null ? store.getBranch().getName() : null);
        return result;
    }
}
