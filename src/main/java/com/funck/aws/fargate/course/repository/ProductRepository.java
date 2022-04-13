package com.funck.aws.fargate.course.repository;

import com.funck.aws.fargate.course.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

}
