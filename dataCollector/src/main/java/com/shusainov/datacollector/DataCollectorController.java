package com.shusainov.datacollector;

import com.shusainov.datacollector.model.StoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
public class DataCollectorController {

    Logger logger = LoggerFactory.getLogger(DataCollectorController.class);
    @Value("${spring.secretToken}")
    private String secretToken;

    @Autowired
    private StoreItemRepository storeItemRepository;

    @GetMapping("/status")
    public String getServerStatus(){
        return "Server is up";
    }

    @PostMapping("/addItem")
    public String addItem(@RequestBody StoreItem item, @RequestParam String token) {
        if (secretToken.equals(token)) {
            item.setLocalTime(new Date());
            storeItemRepository.save(item);
            return item.toString();
        }
        return "Bad token";
    }


}
