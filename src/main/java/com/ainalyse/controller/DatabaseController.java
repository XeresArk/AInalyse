// language: java
package com.ainalyse.controller;

import com.ainalyse.service.DatabaseMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {
    private final DatabaseMetadataService metadataService;

    public DatabaseController(DatabaseMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * GET /api/db/tables
     * Optional query param: schema
     * Example: /api/db/tables?schema=public
     */
    @GetMapping("/tables")
    public ResponseEntity<List<String>> getTables(@RequestParam(value = "schema", required = false) String schema) {
        try {
            List<String> tables = metadataService.getTableNames(schema);
            return ResponseEntity.ok(tables);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}