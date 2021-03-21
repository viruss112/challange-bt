package com.example.app.Entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * This class represents the report entity
 * Table name - reports
 * Primary key - id
 */

@Entity
@Data
public class Report {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "info", columnDefinition = "LONGBLOB")
    private byte[] info;
}
