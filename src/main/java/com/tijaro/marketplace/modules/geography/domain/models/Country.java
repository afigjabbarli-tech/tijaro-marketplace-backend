package com.tijaro.marketplace.modules.geography.domain.models;

import com.tijaro.marketplace.common.domain.models.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "countries")
@Getter
@Setter
public class Country extends BaseEntity {
    @Column(nullable = false, unique = true, length = 100, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(name = "official_name", nullable = false, unique = true, length = 200, columnDefinition = "VARCHAR(200)")
    private String officialName;

    @Column(name = "native_name", nullable = false, unique = true, length = 100, columnDefinition = "VARCHAR(100)")
    private String nativeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "iso2_code", nullable = false, unique = true, length = 2, columnDefinition = "VARCHAR(2)")
    private String iso2Code;

    @Column(name = "iso3_code", nullable = false, unique = true, length = 3, columnDefinition = "VARCHAR(3)")
    private String iso3Code;

    @Column(name = "numeric_code", nullable = false, unique = true, length = 3, columnDefinition = "VARCHAR(3)")
    private String numericCode;

    @Column(name = "phone_code", nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private String phoneCode;

    @Column(name = "capital", length = 100, columnDefinition = "VARCHAR(100)")
    private String capital;

    @Column(columnDefinition = "BIGINT")
    private Long population;

    @Column(name = "area_km2", precision = 15, scale = 2, columnDefinition = "NUMERIC(15,2)")
    private BigDecimal areaKm2;
}
