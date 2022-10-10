package com.YL.reggie.controller;

import com.YL.reggie.common.R;
import com.YL.reggie.dto.DishDto;
import com.YL.reggie.dto.SetmealDto;
import com.YL.reggie.entity.Category;
import com.YL.reggie.entity.Dish;
import com.YL.reggie.entity.DishFlavor;
import com.YL.reggie.entity.Setmeal;
import com.YL.reggie.mapper.DishMapper;
import com.YL.reggie.service.CategoryService;
import com.YL.reggie.service.DishFlavorService;
import com.YL.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝(拷贝除实体类对象集合以外的属性)
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //处理实体类集合
        List<Dish> records = pageInfo.getRecords();

        Iterator<Dish> iterator = records.iterator();

        List<DishDto> list=new ArrayList<>();
        while(iterator.hasNext()){
            Dish record=iterator.next();
            //将Dish的属性考到DishDto里
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record,dishDto);

            Long categoryId = record.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            //根据查询出的分类对象获取分类名称
            String categoryName = category.getName();
            //给DishDto的分类名称赋值
            dishDto.setCategoryName(categoryName);

            list.add(dishDto);
        }

        //给分页构造器对象赋records实体类
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 根据id删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam(value = "ids") List<Long> ids){
        log.info(ids.toString());

        dishService.deleteByIdWithFlavor(ids);

        return R.success("删除成功");
    }

    /**
     * 根据id更改产品状态（启用/禁用）
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> alter(@PathVariable int status
            ,Long[] ids){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        for (Long id:ids) {
            Dish dish=dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("修改成功");
    }

    /**
     * 根据条件查询对应的菜品信息
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1（起售状态）
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1（起售状态）
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        Iterator<Dish> iterator = list.iterator();

        List<DishDto> dishDtoList=new ArrayList<>();

        while(iterator.hasNext()){
            Dish dish1= iterator.next();

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);

            Long categoryId = dish1.getCategoryId();//分类id
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long dish1Id = dish1.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dish1Id);

            List<DishFlavor> dishFlavorList=dishFlavorService.list(lambdaQueryWrapper);

            dishDto.setFlavors(dishFlavorList);

            dishDtoList.add(dishDto);

        }

        return R.success(dishDtoList);
    }
}
