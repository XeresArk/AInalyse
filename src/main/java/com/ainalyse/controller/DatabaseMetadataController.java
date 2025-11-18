package com.ainalyse.controller;

import com.ainalyse.dto.SchemaInfoDTO;
import com.ainalyse.service.DatabaseMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseMetadataController {
    @Autowired
    private DatabaseMetadataService databaseMetadataService;

    @GetMapping("/getTableDetails")
    public ResponseEntity<SchemaInfoDTO> getSchemaDetails() {
        SchemaInfoDTO schemas = databaseMetadataService.getAllSchemasWithTablesAndColumns();
        return ResponseEntity.ok(schemas);
    }
}

