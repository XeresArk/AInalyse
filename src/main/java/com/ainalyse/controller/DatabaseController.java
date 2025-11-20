package com.ainalyse.controller;

import com.ainalyse.dto.ImpactResult;
import com.ainalyse.dto.SchemaInfoDTO;
import com.ainalyse.dto.TableFetchRequest;
import com.ainalyse.service.DatabaseMetadataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseMetadataService databaseMetadataService;
    
    @GetMapping("/getTableDetails")
    public SchemaInfoDTO getSchemaDetails() {
        return databaseMetadataService.getTablesAndColumns();
    }

    @PostMapping("/getDbImpact")
    public ResponseEntity<ImpactResult> getDbImpact(@RequestBody TableFetchRequest request) {
        String schema = (request.getSchemaName() == null || request.getSchemaName().isEmpty()) ? "ainalyse" : request.getSchemaName();
        String table = request.getTableName();
        String column = (request.getColumnName() == null || request.getColumnName().isEmpty()) ? null : request.getColumnName();
        //if columnName is null or empty, pass only schema and table
        return databaseMetadataService.performDatabaseImpactAnalysis(schema, table, column);
    }
}
