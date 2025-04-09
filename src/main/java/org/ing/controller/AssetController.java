package org.ing.controller;

import java.util.List;

import org.ing.dto.AssetRequestDto;
import org.ing.entity.Asset;
import org.ing.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
class AssetController {
	@Autowired
	private AssetService assetService;

	/**
	 * List Assets: List all assets for a given customer id (you can add filters if
	 * you want)
	 * 
	 * @param customerId
	 * @return
	 */
	@GetMapping("/customer/{customerId}")
	public List<Asset> getAssetsByCustomerId(@PathVariable Long customerId) {
		return assetService.getAssetsByCustomerId(customerId);
	}
	
	@GetMapping("/customer/{customerId}/{assetName}")
	public ResponseEntity<Asset> getAssetByCustomerIdAndAssetName(@PathVariable Long customerId, @PathVariable String assetName) {
		Asset asset = assetService.getAssetByCustomerIdAndAssetName(customerId, assetName).orElseThrow();
		return ResponseEntity.ok(asset);
	}

	@PostMapping
	public ResponseEntity<Asset> createAsset(@RequestBody AssetRequestDto assetRequestDto) {
		Asset asset = assetService.createAsset(assetRequestDto.getCustomerId(), assetRequestDto.getAssetName(),
				assetRequestDto.getSize(), assetRequestDto.getUsableSize());
		return ResponseEntity.ok(asset);
	}
}
