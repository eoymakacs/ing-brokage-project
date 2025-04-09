package org.ing.repository;

import java.util.List;
import java.util.Optional;

import org.ing.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
	//public List<Asset> findByCustomerId(Long customerId);

    @Query("SELECT a FROM Asset a WHERE a.user.id = :customerId")
    List<Asset> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Asset a WHERE a.user.id = :customerId and a.assetName = :assetName")
	public Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
}