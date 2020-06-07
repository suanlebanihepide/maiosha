/*
 * @Author: shenzheng
 * @Date: 2020/6/6 22:41
 */

package com.example.springbootmiaosha.Service.model;

import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String name ;
    private String gender;
    private Integer age ;
    private String telphone ;
    private String registerMode ;
    private String thirdPartyId ;
    private String encrptPassword;
}
