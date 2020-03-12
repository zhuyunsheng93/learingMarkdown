package com.example.elasticsearch.repository;

import com.example.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    /**
     * 根据区间查询
     * @param price1
     * @param price2
     * @return
     */
    List<Item> findByPriceBetween(double price1, double price2);
}
