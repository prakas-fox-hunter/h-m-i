package com.example.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.service.BranchesService;
import com.example.library.service.StoresService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import com.example.library.dto.BranchesDto;
import com.example.library.dto.ProvinceByStoreDto;
import com.example.library.dto.SearchStoresDto;
import com.example.library.entity.Branches;
import com.example.library.entity.Stores;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") 
public class ApiController {
    private final StoresService storeService;
    private final BranchesService branchService;

    @GetMapping("/stores/search")
    public List<ProvinceByStoreDto> searchStores(@RequestBody SearchStoresDto request) {
        return storeService.searchStoresByProvinces(request.getProvince(), request.getStore());
    }

    @GetMapping("/branches")
    public ResponseEntity<?> updateBranch(@RequestBody Long id, @RequestBody BranchesDto branch) {
        return new ResponseEntity<>(branchService.updateBranch(id, branch),HttpStatus.OK);
    }

    @DeleteMapping("/brcanches/{id}")
    public ResponseEntity<?> deleteBranch(@RequestParam Long id) {
        return new ResponseEntity<>(branchService.deleteBranch(id),HttpStatus.GONE);
    }

    @GetMapping("/getDataByActiveTrueAndDeletedFalse")
    public ResponseEntity<?> findByActiveTrueAndDeletedFalse(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(storeService.findByActiveTrueAndDeletedFalse(page, size), HttpStatus.OK);
    }

}
