package com.example.elasticsearch;

import com.example.elasticsearch.pojo.Item;
import com.example.elasticsearch.repository.ItemRepository;
import net.minidev.json.JSONUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class IndexTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    @Qualifier("itemRepository")
    private ItemRepository itemRepository;

    @Test
    public void testCreate() {
        elasticsearchTemplate.createIndex(Item.class);
        elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void index() {
        Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        itemRepository.save(item);
    }

    @Test
    public void testQuery(){
        Optional<Item> optional = this.itemRepository.findById(1L);
        System.out.println(optional.get());
    }
    @Test
    public void testFind(){
        Iterable<Item> items = this.itemRepository.findAll(Sort.by(Sort.Direction.DESC,"price"));
        items.forEach(item ->
            System.out.println(item)
        );
    }
    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }
    @Test
    public void queryByPriceBetween(){
        List<Item> list = this.itemRepository.findByPriceBetween(2000.00, 3500.00);
        for (Item item : list) {
            System.out.println("item = " + item);
        }
    }
    @Test
    public void testNativeQuery(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","小米"));
        Page<Item> items  = this.itemRepository.search(queryBuilder.build());
        System.out.println(items.getTotalElements());
        System.out.println(items.getTotalPages());
        items.forEach(System.out::println);
    }
}
