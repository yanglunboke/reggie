package com.YL.reggie.service.impl;

import com.YL.reggie.entity.AddressBook;
import com.YL.reggie.mapper.AddressBookMapper;
import com.YL.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
