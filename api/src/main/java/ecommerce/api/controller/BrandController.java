package ecommerce.api.controller;

import ecommerce.api.dto.BrandApiRequest;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;
import ecommerce.core.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brand")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandApiRequest brand) {
        log.info("REST request to create Brand : {}", brand);
        BrandCoreRequest coreRequest = BrandMapper.toBrandCoreRequest(brand);
        BrandCoreResponse res = brandService.createBrand(coreRequest);
        return ResponseBuilder.success("Create brand success", res);
    }

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        log.info("REST request to get all Brands");
        List<BrandCoreResponse> res = brandService.getAllBrands();
        return ResponseBuilder.success("Get all brands success", res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        log.info("REST request to get Brand by ID : {}", id);
        BrandCoreResponse res = brandService.getBrandById(id);
        return ResponseBuilder.success("Get brand success", res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandApiRequest brand) {
        log.info("REST request to update Brand : {}", id);
        BrandCoreRequest coreRequest = BrandMapper.toBrandCoreRequest(brand);
        BrandCoreResponse res = brandService.updateBrand(id, coreRequest);
        return ResponseBuilder.success("Update brand success", res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        log.info("REST request to delete Brand : {}", id);
        BrandCoreResponse res = brandService.deleteBrand(id);
        return ResponseBuilder.success("Delete brand success", res);
    }
}
