package com.springboot.PersonalAddressBook.contacts.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.PersonalAddressBook.contacts.dto.ContactPage;
import com.springboot.PersonalAddressBook.contacts.entity.Contact;
import com.springboot.PersonalAddressBook.contacts.mapper.ContactsMapper;
import com.springboot.PersonalAddressBook.contacts.service.ContactsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sdx2009
 * @since 2024-07-24
 */
@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contact> implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    // 分页查询联系人
    @Override
    public IPage<Contact> selectContactsPage(ContactPage contactPage, Page<Contact> page) {
        return contactsMapper.selectContactPage(contactPage, page);
    }

    @Override
    public boolean updateContact(Contact updateContact) {
         return this.updateById(updateContact);
    }


    // 新增联系人
    @Override
    public boolean addContact(Contact contact) {
        return this.save(contact);
    }

    // 手机号是否存在
    @Override
    public boolean phoneExist(String phone) {
        int num = contactsMapper.phoneExist(phone);
        return num > 0;
    }


}
