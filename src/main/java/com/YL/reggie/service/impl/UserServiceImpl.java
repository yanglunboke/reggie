package com.YL.reggie.service.impl;

import com.YL.reggie.entity.User;
import com.YL.reggie.mapper.UserMapper;
import com.YL.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
