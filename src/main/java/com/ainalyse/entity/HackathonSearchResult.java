package com.ainalyse.entity;

import lombok.Data;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "HACKATHON_SEARCH_RESULT")
public class HackathonSearchResult {

    @Id
    @Column(name = "RUN_ID")
    private Integer runId;

    @Column(name = "SEARCH_DESC")
    private String searchDesc;

    @Column(name = "OWNER_NAME")
    private String ownerName;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "COLUMN_NAME")
    private String columnName;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "LAST_EDITED_BY")
    private String lastEditedBy;

    @Column(name = "LAST_EDITED_DATE")
    private LocalDate lastEditedDate;

}
