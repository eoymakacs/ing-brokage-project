package org.ing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.ing.entity.Asset;
import org.ing.entity.User;
import org.ing.repository.AssetRepository;
import org.ing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Asset> getAssetsByCustomerId(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }
    
    public Optional<Asset> getAssetByCustomerIdAndAssetName(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    }
    
    public Asset createAsset(Long customerId, String assetName, BigDecimal size, BigDecimal usableSize) {
        // Fetch User entity using customerId
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        // Create and save Asset
        Asset asset = new Asset(customer, assetName, size, usableSize);
        return assetRepository.save(asset);
    }

	
}