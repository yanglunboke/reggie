package com.YL.reggie.service.impl;

import com.YL.reggie.entity.DishFlavor;
import com.YL.reggie.mapper.DishFlavorMapper;
import com.YL.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
