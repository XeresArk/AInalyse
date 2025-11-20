package com.ainalyse.dto;

import lombok.Data;

@Data
public class TableFetchRequest {
    private String schema;
    private String table;
    private String column;
}