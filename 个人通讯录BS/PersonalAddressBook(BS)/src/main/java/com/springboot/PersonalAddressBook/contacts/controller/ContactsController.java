package com.springboot.PersonalAddressBook.contacts.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.PersonalAddressBook.common.result.Result;
import com.springboot.PersonalAddressBook.contacts.dto.ContactPage;
import com.springboot.PersonalAddressBook.contacts.entity.Contact;
import com.springboot.PersonalAddressBook.contacts.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sdx2009
 * @since 2024-07-24
 */
@RestController
@RequestMapping("/personalAddressBook")
public class ContactsController {
    @Autowired
    private ContactsService contactsService;

    // 分页查询
    @GetMapping("/contactPage")
    public Result<?> contactPage(ContactPage contactPage) {
        Page<Contact> page = new Page<>(contactPage.getPageNumber(), contactPage.getPageSize());
        IPage<Contact> ordersIPage = contactsService.selectContactsPage(contactPage, page);
        JSONObject obj = new JSONObject();
        obj.put("total", ordersIPage.getTotal());
        obj.put("rows", ordersIPage.getRecords());

        return new Result<>().success().put(obj);
    }


    // 修改联系人
    @PostMapping("/updateContact")
    public Result<?> updateOrderState(@RequestBody Contact updateContact) {
        boolean result = contactsService.updateContact(updateContact);
        if (result)
            return new Result<>().success();
        else
            return new Result<>().error();
    }



    // 删除联系人
    @GetMapping("/deleteContact")
    public Result<?> deleteTimeOutOrder(Integer id) {
        boolean result = contactsService.removeById(id);
        if(result)
            return new Result<>().success();
        else
            return new Result<>().error();

    }

    // 添加联系人
    @PostMapping("/addContact")
    public Result<?> addContact(@RequestBody Contact contact) {
        boolean result = contactsService.addContact(contact);
        if(result)
            return new Result<>().success();
        else
            return new Result<>().error();
    }

    // 手机号是否存在
    @GetMapping("/phoneExist")
    public Result<?> phoneExist(String phone) {
        boolean res = contactsService.phoneExist(phone);
        return new Result<>().success().put(res);
    }


}
