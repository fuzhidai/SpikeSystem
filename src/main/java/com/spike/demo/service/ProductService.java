package com.spike.demo.service;

import com.spike.demo.dao.ProductDao;
import com.spike.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Transactional
    public Product getProductById(Long productId) {
        return productDao.getProductById(productId);
    }

    @Transactional
    public int decreaseProductStock(Long productId) {
        return productDao.decreaseProductStock(productId);
    }

    @Transactional
    public List<Product> getAllProduct() {
        return productDao.selectAllProducts();
    }
}
