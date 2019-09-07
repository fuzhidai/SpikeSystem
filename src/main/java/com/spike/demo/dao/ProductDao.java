package com.spike.demo.dao;

import com.spike.demo.model.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {

    @Select("select * from product")
    List<Product> selectAllProducts();

    @Select("select * from product where id = #{id}")
    Product getProductById(@Param("id") Long id);

    @Update("Update product set stock=stock-1 where id=#{id} and stock>0")
    int decreaseProductStock(@Param("id") Long id);

    @Delete("Delete from product where id=#{id}")
    void deleteProduct(@Param("id") Long id);
}
