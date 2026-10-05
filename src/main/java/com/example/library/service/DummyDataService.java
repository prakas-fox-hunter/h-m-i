package com.example.library.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.entity.Branches;
import com.example.library.entity.Provinces;
import com.example.library.entity.Stores;
import com.example.library.repository.BranchesRepository;
import com.example.library.repository.ProvincesRepository;
import com.example.library.repository.StoresRepository;

import jakarta.annotation.PostConstruct;

@Service
public class DummyDataService {
    @Autowired
    private BranchesRepository branchRepo;

    @Autowired
    private StoresRepository storesRepository;

    @Autowired
    private ProvincesRepository provincesRepository;

    @PostConstruct
    public void createDummyData() {
        // Daftar provinsi di Indonesia (contoh sebagian, bisa lengkapkan sesuai
        // kebutuhan)
        String[] provinceNames = {
                "Aceh", "Bali", "Banten", "Bengkulu", "DI Yogyakarta", "DKI Jakarta",
                "Gorontalo", "Jambi", "Jawa Barat", "Jawa Tengah", "Jawa Timur",
                "Kalimantan Barat", "Kalimantan Selatan", "Kalimantan Tengah", "Kalimantan Timur",
                "Kalimantan Utara", "Kepulauan Bangka Belitung", "Kepulauan Riau", "Lampung",
                "Maluku", "Maluku Utara", "Nusa Tenggara Barat", "Nusa Tenggara Timur",
                "Papua", "Papua Barat", "Riau", "Sulawesi Barat", "Sulawesi Selatan",
                "Sulawesi Tengah", "Sulawesi Tenggara", "Sulawesi Utara", "Sumatera Barat",
                "Sumatera Selatan", "Sumatera Utara"
        };

        // Membuat data untuk Provinsi
        List<Provinces> provincesList = new ArrayList<>();
        for (String name : provinceNames) {
            Provinces province = new Provinces();
            province.setName(name);
            provincesRepository.save(province);
            provincesList.add(province);
        }

        // Membuat 100 Branch secara merata di seluruh provinsi
        int totalBranches = 100;
        List<Branches> branchList = new ArrayList<>();
        for (int i = 1; i <= totalBranches; i++) {
            Branches branch = new Branches();
            branch.setName("Branch " + i);
            branch.setActive(true);
            branch.setDeleted(false);
            branch.setProvince(provincesList.get(i % provincesList.size())); // Distribusi merata
            branchRepo.save(branch);
            branchList.add(branch);
        }

        // Membuat 20.000 Store secara merata di seluruh branch
        int totalStores = 20000;
        for (int i = 1; i <= totalStores; i++) {
            Stores store = new Stores();
            store.setName("Store " + i);
            store.setActive(true);
            store.setWhitelisted(i % 10 == 0); // Setiap 10 store menjadi whitelisted
            store.setBranch(branchList.get(i % branchList.size())); // Assign branch secara round-robin
            storesRepository.save(store);
        }

        System.out.println("Dummy data berhasil dibuat: " + provincesList.size() + " provinsi, "
                + branchList.size() + " branch, " + totalStores + " store.");
    }

}
