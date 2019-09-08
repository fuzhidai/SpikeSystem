package com.spike.demo.controller;

import com.spike.demo.global.Constants;
import com.spike.demo.model.Order;
import com.spike.demo.model.Product;
import com.spike.demo.service.OrderService;
import com.spike.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @PostConstruct
    public void initRedis() {
        // 将数据库中所有商品的库存信息同步到 Redis 中
        List<Product> products = productService.getAllProduct();
        for (Product product: products) {
            stringRedisTemplate.opsForValue().set(Constants.PRODUCT_STOCK_PREFIX + product.getId(), product.getStock() + "");
        }
    }

    @PostMapping("/{productId}")
    public String spike(@PathVariable("productId") Long productId) {

        try {
            Long stock = stringRedisTemplate.opsForValue().decrement(Constants.PRODUCT_STOCK_PREFIX + productId);
            if (stock < 0) {
                // 保证 Redis 当中的商品库存恒为非负数
                stringRedisTemplate.opsForValue().increment(Constants.PRODUCT_STOCK_PREFIX + productId);
                throw new RuntimeException("The product has been sold out.");
            }
            orderService.spike(productId);
        } catch (Exception e){
            // 回滚 Redis 中的库存
            stringRedisTemplate.opsForValue().increment(Constants.PRODUCT_STOCK_PREFIX + productId);
            return "fail";
        }
        return "success";
    }
}
