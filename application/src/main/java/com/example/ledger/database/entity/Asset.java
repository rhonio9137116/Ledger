package com.example.ledger.database.entity;

import com.example.ledger.database.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Asset extends BaseEntity {

    @Column
    private String assetName;

    @Column
    private String assetType;
}
