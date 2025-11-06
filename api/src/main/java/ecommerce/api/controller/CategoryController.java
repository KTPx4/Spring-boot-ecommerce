package ecommerce.api.controller;

import ecommerce.api.dto.CategoryApiRequest;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;
import ecommerce.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryApiRequest category) {
        log.info("REST request to create Category : {}", category);
        CategoryCoreRequest coreRequest = CategoryMapper.toCategoryCoreRequest(category);
        CategoryCoreResponse res = categoryService.createCategory(coreRequest);
        return ResponseBuilder.success("Create category success", res);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        log.info("REST request to get all Categories");
        List<CategoryCoreResponse> res = categoryService.getAllCategories();
        return ResponseBuilder.success("Get all categories success", res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        log.info("REST request to get Category by ID : {}", id);
        CategoryCoreResponse res = categoryService.getCategoryById(id);
        return ResponseBuilder.success("Get category success", res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryApiRequest category) {
        log.info("REST request to update Category : {}", id);
        CategoryCoreRequest coreRequest = CategoryMapper.toCategoryCoreRequest(category);
        CategoryCoreResponse res = categoryService.updateCategory(id, coreRequest);
        return ResponseBuilder.success("Update category success", res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        log.info("REST request to delete Category : {}", id);
        CategoryCoreResponse res = categoryService.deleteCategory(id);
        return ResponseBuilder.success("Delete category success", res);
    }
}
