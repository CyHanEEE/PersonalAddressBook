package com.springboot.PersonalAddressBook.contacts.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.PersonalAddressBook.contacts.dto.ContactPage;
import com.springboot.PersonalAddressBook.contacts.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sdx2009
 * @since 2024-07-24
 */
@Mapper
public interface ContactsMapper extends BaseMapper<Contact> {
    // 分页查询订单
    IPage<Contact> selectContactPage(ContactPage contactPage, Page<Contact> page);

    // 手机号是否存在
    Integer phoneExist(String phone);
}
