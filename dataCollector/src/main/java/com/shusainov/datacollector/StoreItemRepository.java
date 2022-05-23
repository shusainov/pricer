package com.shusainov.datacollector;

import com.shusainov.datacollector.model.StoreItem;
import org.springframework.data.repository.CrudRepository;

public interface StoreItemRepository extends CrudRepository<StoreItem, Integer> {

}