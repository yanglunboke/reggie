package com.YL.reggie.mapper;

import com.YL.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.tomcat.jni.Address;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
