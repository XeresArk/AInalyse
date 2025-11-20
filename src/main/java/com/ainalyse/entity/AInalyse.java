package com.ainalyse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class AInalyse {
    @Id
    private Long id;
}
