package com.suite.suite_anp_service.baseTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public class BaseTimeMongoEntity {

    @CreatedDate
    @Field(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "updated_date")
    private LocalDateTime updatedDate;
}