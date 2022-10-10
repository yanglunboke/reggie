package com.YL.reggie.service;

import com.YL.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {

    public void submit(Orders order);
}
