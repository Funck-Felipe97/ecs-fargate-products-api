package com.funck.aws.fargate.course.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.funck.aws.fargate.course.model.Invoice;
import com.funck.aws.fargate.course.model.UrlResponse;
import com.funck.aws.fargate.course.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final String bucketName;
    private final AmazonS3 amazonS3;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(@Value("${aws.s3.bucket.invoice-events}") final String bucketName, final AmazonS3 amazonS3, final InvoiceRepository invoiceRepository) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> createInvoiceUrl() {
        final var expirationTime = Instant.now().plus(Duration.ofMinutes(5));
        final var processId = UUID.randomUUID().toString();

        final var generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, processId)
                .withMethod(HttpMethod.PUT)
                .withExpiration(Date.from(expirationTime));

        final var urlResponse = UrlResponse.builder()
                .url(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .expirationTime(expirationTime.toEpochMilli())
                .build();

        return new ResponseEntity<>(urlResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/filter")
    public Iterable<Invoice> findAllByCustomerName(@RequestParam final String customerName) {
        return invoiceRepository.findAllByCustomerName(customerName);
    }

}
