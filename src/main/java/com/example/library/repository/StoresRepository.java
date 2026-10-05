package com.example.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.library.dto.ActiveAndNonDeleteDataDto;
import com.example.library.dto.ProvinceByStoreDto;
import com.example.library.dto.WhiteListedStoreDto;
import com.example.library.entity.Stores;

@Repository
public interface StoresRepository extends JpaRepository<Stores, Long> {
     @Query(nativeQuery = true, value = "SELECT s FROM Store s " +
           "WHERE s.active=true AND s.deleted=false " +
           "AND (LOWER(s.branch.province.name) LIKE LOWER(CONCAT('%', :provinceName, '%')) " +
           "     OR s.whitelisted=true)")
    List<Stores> findActiveByProvinceNameOrWhitelisted(@Param("provinceName") String provinceName);

    @Query(nativeQuery = true, value = "select\r\n" + //
                "\ts.id as storeId,\r\n" + //
                "\ts.name as storeName ,\r\n" + //
                "\tp.name as provinceName \r\n" + //
                "from\r\n" + //
                "\tstores s\r\n" + //
                "inner join branches b on\r\n" + //
                "\ts.branch_id = b.id\r\n" + //
                "inner join provinces p on\r\n" + //
                "\tb.province_id = p.id\r\n" + //
                "where\r\n" + //
                "\tp.\"name\" like '%' || :province || '%' and s.\"name\"  like '%' || :store || '%' ; ")
    List<ProvinceByStoreDto> findByBranch_Province_NameContainingIgnoreCaseAndNameContainingIgnoreCase(@Param("province") String province, @Param("store") String store);

    @Query(nativeQuery = true, value = "select\r\n" + //
                "\tb.id,\r\n" + //
                "\tb.active as active_branch,\r\n" + //
                "\tb.deleted as delete_branch,\r\n" + //
                "\tb.name as name_branch,\r\n" + //
                "\ts.active as active_store,\r\n" + //
                "\ts.deleted as delete_store,\r\n" + //
                "\ts.name as name_store, \r\n" + //
                "\tp.active as active_provinces,\r\n" + //
                "\tp.deleted as delete_provinces,\r\n" + //
                "\tp.name as name_provinces\r\n" + //
                "from\r\n" + //
                "\tstores s\r\n" + //
                "inner join branches b on\r\n" + //
                "\ts.branch_id = b.id\r\n" + //
                "inner join provinces p on\r\n" + //
                "\tb.province_id = p.id where b.active = true and p.active = true and s.active = true and b.deleted = false and p.deleted = false and s.deleted = false;")
    Page<ActiveAndNonDeleteDataDto> findByActiveTrueAndDeletedFalse(Pageable pageable);

    @Query(nativeQuery = true, value = "select\r\n" + //
                "\ts.id ,\r\n" + //
                "\ts.name as name_store,\r\n" + //
                "\ts.active as active_store,\r\n" + //
                "\ts.deleted as delete_store,\r\n" + //
                "\tp.\"name\" as nama_province,\r\n" + //
                "\ts.whitelisted\r\n" + //
                "from\r\n" + //
                "\tstores s\r\n" + //
                "inner join branches b on\r\n" + //
                "\ts.branch_id = b.id\r\n" + //
                "inner join provinces p on\r\n" + //
                "\tb.province_id = p.id where s.whitelisted = true;")
    List<WhiteListedStoreDto> findByWhitelisted();
}
