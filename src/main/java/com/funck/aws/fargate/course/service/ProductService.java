package com.funck.aws.fargate.course.service;

import com.funck.aws.fargate.course.exceptions.NotFoundException;
import com.funck.aws.fargate.course.model.EventType;
import com.funck.aws.fargate.course.model.Product;
import com.funck.aws.fargate.course.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductPublisher publisher;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Product save(Product product) {
        var savedProduct = repository.save(product);

        publisher.publishProductEvent(savedProduct, EventType.CREATED);

        return savedProduct;
    }

    public Product update(Product product, Long id) {
        return findById(id)
                .map(it -> {
                    product.setId(id);
                    var savedProduct = repository.save(product);
                    publisher.publishProductEvent(savedProduct, EventType.UPDATED);
                    return savedProduct;
                }).orElseThrow(() -> new NotFoundException(String.format("Product not found with id: %s", id)));
    }

    public void deleteById(Long id) {
        findById(id)
                .map(product -> {
                    repository.delete(product);
                    publisher.publishProductEvent(product, EventType.DELETED);
                    return product;
                }).orElseThrow(() -> new NotFoundException(String.format("Product not found with id: %s", id)));
    }
}
