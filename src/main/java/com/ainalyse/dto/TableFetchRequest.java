package com.ainalyse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableFetchRequest {
    private String schemaName;
    private String tableName;
    private String columnName;
}
