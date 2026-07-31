package com.ecommerce.backend.product_module;

import com.ecommerce.backend.common.PageResponse;
import com.ecommerce.backend.product_module.dto.ProductRequest;
import com.ecommerce.backend.product_module.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create Product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Only ADMIN can create products")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {

        return new ResponseEntity<>(
                productService.createProduct(request),
                HttpStatus.CREATED
        );

    }


    @Operation(summary = "Get All Products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all products")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return new ResponseEntity<>(
                productService.getAllProducts(),
                HttpStatus.OK
        );

    }


    @Operation(summary = "Get Product By ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ) {

        return new ResponseEntity<>(
                productService.getProductById(id),
                HttpStatus.OK
        );

    }



    @Operation(summary = "Update Product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Only ADMIN can update")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {

        return new ResponseEntity<>(
                productService.updateProduct(id, request),
                HttpStatus.OK
        );

    }



    @Operation(summary = "Delete Product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Only ADMIN can delete")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "Get Products By Category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products returned"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<ProductResponse>>
    getProductsByCategory(
            @PathVariable Long categoryId
    ) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryId)
        );

    }



    @Operation(summary = "Get Products With Pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved")
    })
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction

    ) {

        return ResponseEntity.ok(
                productService.getProducts(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );

    }


    @Operation(summary = "Search Products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matching products returned")
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<ProductResponse>> searchProducts(

            @RequestParam String keyword

    ) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword)
        );

    }



}