package com.spike.demo.dao;

import com.spike.demo.model.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {

    @Select("select * from spike_order")
    List<Order> selectAllOrders();

    @Select("select * from spike_order where id = #{id}")
    Order getOrderById(@Param("id") Long id);

    @Insert("insert into spike_order(id, product_id, amount) values (#{id}, #{productId}, #{amount})")
    void insertOrder(Order order);

    @Update("Update spike_order set product_id=#{productId}, amount=#{amount} where id=#{id}")
    void updateOrder(Order order);

    @Delete("Delete from spike_order where id=#{id}")
    void deleteOrder(@Param("id") Long id);
}
