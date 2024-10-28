package com.springboot.PersonalAddressBook.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/*
* 字段自动填充
* */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 新增订单时对orderTime自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        Object orderTime = getFieldValByName("orderTime", metaObject);

        //如果createTime为空，表示用户没有对该字段进行处理，可以自动填充，否则不能进行自动填充
        if(ObjectUtils.isEmpty(orderTime))
            setFieldValByName("orderTime", LocalDateTime.now(), metaObject);
    }

    //更新操作时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if(ObjectUtils.isEmpty(updateTime))
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

    }
}
