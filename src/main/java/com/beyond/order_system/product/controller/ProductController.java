package com.beyond.order_system.product.controller;

import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.dto.ProductCreateReqDto;
import com.beyond.order_system.product.dto.ProductDetailResDto;
import com.beyond.order_system.product.dto.ProductListResDto;
import com.beyond.order_system.product.dto.ProductSearchReqDto;
import com.beyond.order_system.product.service.ProductService;
import jakarta.persistence.Access;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@ModelAttribute ProductCreateReqDto dto,
                                    @RequestParam(value="productImage")MultipartFile productImage,
                                    @AuthenticationPrincipal String principal){
        Product product = productService.create(dto, productImage, principal);
        return ResponseEntity.status(HttpStatus.OK).body(product.getId());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        ProductDetailResDto dto = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                     @ModelAttribute ProductSearchReqDto searchDto){
        Page<ProductListResDto> productListResDtoList = productService.findAll(searchDto, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(productListResDtoList);
    }
}
