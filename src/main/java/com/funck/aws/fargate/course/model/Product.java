package com.funck.aws.fargate.course.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Setter
@Getter
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 56)
    private String name;

    @Column(nullable = false, length = 56)
    private String model;

    @Column(nullable = false, length = 8)
    private String code;

    @Column(nullable = false)
    private Float price;

}
