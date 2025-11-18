package com.ainalyse.controller;

import com.ainalyse.dto.SchemaInfoDTO;
import com.ainalyse.dto.TableFetchRequest;
import com.ainalyse.service.DatabaseMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseMetadataController {
    @Autowired
    private DatabaseMetadataService databaseMetadataService;

    @GetMapping("/getTableDetails")
    public SchemaInfoDTO getSchemaDetails() {
        return databaseMetadataService.getAllSchemasWithTablesAndColumns();
    }

    @PostMapping("/getDBImpact")
    public List getImpactResult(@RequestBody TableFetchRequest request) throws SQLException {

        String schema = request.getSchemaName();
        String table = request.getTableName();
        String column = (request.getColumnName() == null || request.getColumnName().isEmpty()) ? null : request.getColumnName();
        //if columnName is null or empty, pass only schema and table
        return databaseMetadataService.performDatabaseImpactAnalysis(schema, table, column);

    }
}

