package com.funck.aws.fargate.course.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number"})
        }
)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String number;

    @Column(length = 32, nullable = false)
    private String customerName;

    private Float totalValue;

    private Long productId;

    private Integer quantity;

}
