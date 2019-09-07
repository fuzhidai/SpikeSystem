package com.spike.demo.controller;

import com.spike.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{productId}")
    public String spike(@PathVariable("productId") Long productId) {

        try {
            orderService.spike(productId);
        } catch (Exception e){
            return "fail";
        }

        return "success";
    }
}
