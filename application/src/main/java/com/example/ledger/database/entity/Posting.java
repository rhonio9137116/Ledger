package com.example.ledger.database.entity;

import com.example.ledger.database.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Posting extends BaseEntity {

    public static String STATUS_PENDING = "PENDING";
    public static String STATUS_CLEARED = "CLEARED";
    public static String STATUS_FAILED = "FAILED";

    @Column
    private Long userId;

    private String description;

    @Column
    private String status;

    @OneToMany(mappedBy = "posting")
    private List<PostingAssetMovement> postingAssetMovements = new ArrayList<>();
}
