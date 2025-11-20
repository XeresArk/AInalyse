package com.ainalyse.dto;

import lombok.Data;

@Data
public class TableFetchRequest {
    private String schemaName;
    private String tableName;
    private String columnName;
}