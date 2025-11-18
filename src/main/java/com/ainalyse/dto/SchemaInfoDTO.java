package com.ainalyse.dto;

import java.util.List;
import java.util.Map;

public class SchemaInfoDTO {
    private String schemaName;
    private Map<String, List<String>> tables;

    public SchemaInfoDTO() {}
    public SchemaInfoDTO(String schemaName, Map<String, List<String>> tables) {
        this.schemaName = schemaName;
        this.tables = tables;
    }
    public String getSchemaName() { return schemaName; }

    public void setSchemaName(String schemaName) { this.schemaName = schemaName; }

    public Map<String, List<String>> getTables() { return tables; }

    public void setTables(Map<String, List<String>> tables) { this.tables = tables; }
}

