/*
 * @Author: shenzheng
 * @Date: 2020/6/6 22:54
 */

package com.example.springbootmiaosha.Controller.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;
    private String name ;
    private Byte gender;
    private Integer age ;
    private String telphone ;
}
