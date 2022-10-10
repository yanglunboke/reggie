package com.YL.reggie.service.impl;

import com.YL.reggie.dto.DishDto;
import com.YL.reggie.entity.Dish;
import com.YL.reggie.entity.DishFlavor;
import com.YL.reggie.mapper.DishMapper;
import com.YL.reggie.service.DishFlavorService;
import com.YL.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId=dishDto.getId();//菜品id

        //给菜品口味设置菜品id
        List<DishFlavor> flavors=dishDto.getFlavors();

        Iterator<DishFlavor> iterator = flavors.iterator();
        while (iterator.hasNext()){
            DishFlavor flavor = iterator.next();
            flavor.setDishId(dishId);
        }

        //保存菜品口味数据到口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息,从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        //给菜品口味设置菜品id
        Iterator<DishFlavor> iterator = flavors.iterator();
        while (iterator.hasNext()){
            DishFlavor flavor = iterator.next();
            flavor.setDishId(dishDto.getId());
        }

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id删除菜品信息及对应的口味信息
     * @param ids
     */
    @Override
    public void deleteByIdWithFlavor(List<Long> ids) {
        //根据id删除菜品
        this.removeByIds(ids);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        for (Long id:ids) {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id);

            dishFlavorService.remove(queryWrapper);
        }
    }
}
