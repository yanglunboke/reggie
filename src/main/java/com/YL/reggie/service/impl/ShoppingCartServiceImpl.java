package com.YL.reggie.service.impl;

import com.YL.reggie.entity.ShoppingCart;
import com.YL.reggie.mapper.ShoppingCartMapper;
import com.YL.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
