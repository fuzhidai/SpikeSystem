package com.spike.demo.mq;

import com.spike.demo.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "order")
public class Consumer {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void consume(@Payload Long productId) {
        orderService.spike(productId);
    }
}
