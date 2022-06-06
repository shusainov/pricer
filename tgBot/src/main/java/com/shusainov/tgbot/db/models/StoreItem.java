package com.shusainov.tgbot.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class StoreItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String link;
    private int price;
    @Temporal(TemporalType.TIMESTAMP)
    private Date localTime;
    private String dataSetName;
    private String storeName;
}
