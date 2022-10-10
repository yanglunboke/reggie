package com.YL.reggie.dto;

import com.YL.reggie.entity.Setmeal;
import com.YL.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
