package com.springboot.PersonalAddressBook.contacts.dto;

import lombok.Data;

@Data
public class ContactPage {
    // 分页字段
    private Long pageNumber;
    private Long pageSize;

    private Integer Id;
    private String Name;
    private String Address;
    private String Phone;

}
