package com.YL.reggie.service.impl;

import com.YL.reggie.entity.Employee;
import com.YL.reggie.mapper.EmployeeMapper;
import com.YL.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
