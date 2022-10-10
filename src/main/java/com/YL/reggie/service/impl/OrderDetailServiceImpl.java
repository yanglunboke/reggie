package com.YL.reggie.service.impl;

import com.YL.reggie.entity.OrderDetail;
import com.YL.reggie.mapper.OrderDetailMapper;
import com.YL.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
