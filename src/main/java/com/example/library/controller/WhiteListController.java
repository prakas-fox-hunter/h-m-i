package com.example.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dto.BranchesDto;
import com.example.library.dto.StoresDto;
import com.example.library.dto.WhiteListedStoreDto;
import com.example.library.entity.Stores;
import com.example.library.repository.StoresRepository;
import com.example.library.service.StoresService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/whitelist")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class WhiteListController {

    private final StoresService storeService;

    @GetMapping("")
    public List<WhiteListedStoreDto> getWhitelistedStores() {
        return storeService.findByWhitelisted();
    }

    @GetMapping("/Store/{id}")
    public ResponseEntity<?> updateStore(@RequestBody Long id, @RequestBody StoresDto store) {
        return new ResponseEntity<>(storeService.updateStore(id, store), HttpStatus.OK);
    }

    @DeleteMapping("/store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.deleteStore(id), HttpStatus.GONE);
    }


}
