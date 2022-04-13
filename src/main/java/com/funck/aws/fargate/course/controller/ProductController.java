package com.funck.aws.fargate.course.controller;

import com.funck.aws.fargate.course.exceptions.NotFoundException;
import com.funck.aws.fargate.course.model.Product;
import com.funck.aws.fargate.course.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable final Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody final Product product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productRepository.save(product));
    }

    @PutMapping("{id}")
    public Product update(@RequestBody final Product product, @PathVariable final Long id) {
        return productRepository.findById(id)
                .map(it -> {
                    product.setId(id);
                    return productRepository.save(product);
                }).orElseThrow(() -> new NotFoundException(String.format("Product not found with id: %s", id)));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable final Long id) {
        productRepository.deleteById(id);
    }

}
