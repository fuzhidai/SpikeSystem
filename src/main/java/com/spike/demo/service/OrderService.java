package com.spike.demo.service;

import com.spike.demo.dao.OrderDao;
import com.spike.demo.model.Order;
import com.spike.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDao orderDao;

    @Transactional
    public void spike(Long productId) {

        // 查询商品
        Product product = productService.getProductById(productId);

        if (product.getStock() <= 0){
            throw new RuntimeException("The product has been sold out.");
        }

        // 创建秒杀订单
        Order order = new Order();
        order.setProductId(product.getId());
        order.setAmount(product.getPrice());
        saveOrder(order);

        // 减库存
        int updateNum = productService.decreaseProductStock(productId);
        if (updateNum <= 0){
            throw new RuntimeException("The product has been sold out.");
        }
    }

    @Transactional
    public void saveOrder(Order order) {
        orderDao.insertOrder(order);
    }
}
