package com.funck.aws.fargate.course.consumer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funck.aws.fargate.course.model.Invoice;
import com.funck.aws.fargate.course.model.SnsMessage;
import com.funck.aws.fargate.course.repository.InvoiceRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Component
public class InvoiceConsumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final InvoiceRepository invoiceRepository;
    private final AmazonS3 amazonS3;

    public InvoiceConsumer(final InvoiceRepository invoiceRepository, final AmazonS3 amazonS3) {
        this.invoiceRepository = invoiceRepository;
        this.amazonS3 = amazonS3;
    }

    @SneakyThrows
    @JmsListener(destination = "${aws.sqs.queue.invoice.events}")
    public void receiveS3Event(final TextMessage textMessage) {
        log.info("Message received from invoice events queue {}", textMessage);

        final var snsMessage = MAPPER.readValue(textMessage.getText(), SnsMessage.class);
        final var s3EventNotification = MAPPER.readValue(snsMessage.getMessage(), S3EventNotification.class);
    
        processInvoiceNotification(s3EventNotification);
    }

    private void processInvoiceNotification(S3EventNotification s3EventNotification) {
        s3EventNotification.getRecords().forEach(it -> {
            final var s3Item = it.getS3();
            final var bucketName = s3Item.getBucket().getName();
            final var objectKey = s3Item.getObject().getKey();

            final var invoiceFile = downloadObject(bucketName, objectKey);

            try {
                var invoice = MAPPER.readValue(invoiceFile, Invoice.class);

                log.info("Invoice received: {}", invoice.getNumber());

                invoiceRepository.save(invoice);

                amazonS3.deleteObject(bucketName, objectKey);
            } catch (JsonProcessingException e) {
                log.error("Error on receive invoice");
                throw new RuntimeException(e);
            }
        });
    }

    @SneakyThrows
    private String downloadObject(final String bucketName, final String objectKey) {
        final var s3Object = amazonS3.getObject(bucketName, objectKey);
        final var stringBuilder = new StringBuilder();
        final var bufferedReader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        String content = null;

        while ((content = bufferedReader.readLine()) != null) {
            stringBuilder.append(content);
        }

        return stringBuilder.toString();
    }

}
