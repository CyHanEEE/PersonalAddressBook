package com.springboot.PersonalAddressBook.contacts.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.PersonalAddressBook.contacts.dto.ContactPage;
import com.springboot.PersonalAddressBook.contacts.entity.Contact;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sdx2009
 * @since 2024-07-24
 */
public interface ContactsService extends IService<Contact> {
    // 分页查询联系人
    IPage<Contact> selectContactsPage(ContactPage contactPage, Page<Contact> page);

    // 修改联系人
    boolean updateContact(Contact updateContact);

    // 新增联系人
    boolean addContact(Contact contact);

    // 手机号是否存在
    boolean phoneExist(String phone);
}
