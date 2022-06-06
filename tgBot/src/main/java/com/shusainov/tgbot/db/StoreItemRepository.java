package com.shusainov.tgbot.db;

import com.shusainov.tgbot.db.models.StoreItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreItemRepository extends CrudRepository<StoreItem, Integer> {

    @Query("SELECT s FROM StoreItem s WHERE s.dataSetName LIKE %:dataSet% ORDER BY id DESC")
    List<StoreItem> searchLastByLike(@Param("dataSet") String dataSet);


}