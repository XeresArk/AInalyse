// language: java
package com.ainalyse.service;

import com.ainalyse.dto.*;
import com.ainalyse.repository.ImpactRepository;
import com.ainalyse.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseMetadataService {
    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private ImpactRepository impactRepository;

    String schemaName = "ainalyse";
    @Transactional
    public SchemaInfoDTO getTablesAndColumns() {

        Map<String, List<String>> result = new LinkedHashMap<>();

        List<TableNameInterface> tables = metadataRepository.getTables();

        for (TableNameInterface table : tables) {

            List<String> columns = metadataRepository.getColumns(table.getTableName())
                    .stream()
                    .map(ColumnInfoInterface::getColumnName)
                    .toList();

            result.put(table.getTableName(), columns);
        }

        return new SchemaInfoDTO(schemaName, result);
    }

    @Transactional
    public ImpactResult performDatabaseImpactAnalysis(String schema, String table, String column) {

        // If column is empty/null, pass NULL to procedure
        String columnValue = (column == null || column.isBlank()) ? null : column;

        impactRepository.callImpactProcedure(schema, table, columnValue);

        List<String> response= impactRepository.findLatestSearchDescList();
        ImpactResult impactResult = new ImpactResult();
        // Convert List<String> → List<ImpactElement>
        List<ImpactElement> directImpacts = response.stream()
                .map(desc -> {
                    ImpactElement e = new ImpactElement();
                    e.setName(desc);
                    return e;
                })
                .toList();

        // Build final ImpactResult
        ImpactResult result = new ImpactResult();
        result.setDirectImpacts(directImpacts);

        return result;
    }
}
