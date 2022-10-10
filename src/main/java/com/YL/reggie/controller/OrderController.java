package com.YL.reggie.controller;

import com.YL.reggie.common.BaseContext;
import com.YL.reggie.common.R;
import com.YL.reggie.dto.OrdersDto;
import com.YL.reggie.entity.OrderDetail;
import com.YL.reggie.entity.Orders;
import com.YL.reggie.service.OrderDetailService;
import com.YL.reggie.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param order
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        log.info("订单数据:{}",order);
        orderService.submit(order);
        return R.success("下单成功");
    }

    /**
     * 分页查询订单数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> UserPage(int page,int pageSize){
        Long currentId = BaseContext.getCurrentId();

        Page<Orders> pageInfo=new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage=new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getUserId,currentId);

        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> orders=pageInfo.getRecords();

        List<OrdersDto> orderDtoList=new ArrayList<>();

        Iterator<Orders> iterator = orders.iterator();
        while(iterator.hasNext()){
            Orders order = iterator.next();

            OrdersDto ordersDto=new OrdersDto();

            BeanUtils.copyProperties(order,ordersDto);

            String number = order.getNumber();
            LambdaQueryWrapper<OrderDetail> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId,number);
            List<OrderDetail> list = orderDetailService.list(queryWrapper1);

            int num=0;
            for(OrderDetail orderDetail:list){
                num+=orderDetail.getNumber().intValue();
            }

            ordersDto.setSumNum(num);
            ordersDto.setOrderDetails(list);

            orderDtoList.add(ordersDto);
        }

        ordersDtoPage.setRecords(orderDtoList);

        return R.success(ordersDtoPage);
    }

    /**
     * 后端订单分页查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number,String beginTime,String endTime){

        Page<Orders> pageInfo=new Page(page,pageSize);
        Page<OrdersDto> ordersDtoPage=new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.like(number!=null,Orders::getNumber,number);

        queryWrapper.orderByAsc(Orders::getOrderTime);

        if(beginTime!=null&&endTime!=null){
            queryWrapper.ge(Orders::getOrderTime,beginTime);
            queryWrapper.le(Orders::getOrderTime,endTime);
        }

        orderService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();
        ArrayList<OrdersDto> list = new ArrayList<>();

        Iterator<Orders> iterator = records.iterator();
        while(iterator.hasNext()){
            Orders order = iterator.next();

            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(order,ordersDto);

            ordersDto.setUserName("用户id:"+order.getUserId());

            list.add(ordersDto);
        }

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }

    /**
     * 更改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> alter(@RequestBody Orders orders){
        Long id = orders.getId();
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);

        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(orders.getStatus());

        orderService.updateById(one);
        return R.success("修改成功");
    }
}
