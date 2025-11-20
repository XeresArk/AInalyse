// language: java
package com.ainalyse.service;

import com.ainalyse.dto.ColumnInfoInterface;
import com.ainalyse.dto.ImpactElement;
import com.ainalyse.dto.ImpactResult;
import com.ainalyse.dto.SchemaInfoDTO;
import com.ainalyse.dto.TableNameInterface;
import com.ainalyse.repository.ImpactRepository;
import com.ainalyse.repository.MetadataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PersistenceContext
    private EntityManager entityManager;

    String schemaName = "ainalyse";
    @Transactional
    public SchemaInfoDTO getTablesAndColumns() {

        Map<String, List<String>> result = new LinkedHashMap<>();
        List<TableNameInterface> tables = metadataRepository.getTables(schemaName);

        for (TableNameInterface table : tables) {
            List<String> columns = metadataRepository.getColumns(schemaName, table.getTableName())
                    .stream()
                    .map(ColumnInfoInterface::getColumnName)
                    .toList();
            result.put(table.getTableName(), columns);
        }
        return new SchemaInfoDTO(schemaName, result);
    }

    @Transactional
    public ResponseEntity<ImpactResult> performDatabaseImpactAnalysis(String schema, String table, String column) {

        impactRepository.callImpactProcedure(schema, table, column);

        List<String> response= impactRepository.findLatestSearchDescList();
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

        return ResponseEntity.ok(result);
    }
}
