/*
 * @Author: shenzheng
 * @Date: 2020/6/6 23:02
 */

package com.example.springbootmiaosha.Controller.response;

import lombok.Data;

@Data

public class CommonReturnType {


    //固定返回信息
    private String status;
    private Object data;

    public CommonReturnType() {

    }

    public CommonReturnType(String status, Object result) {
        this.status = status;
        this.data = result;
    }

    //定义通用创建方法
    public static CommonReturnType create(Object result) {

        return new CommonReturnType("success", result);
    }

    public static CommonReturnType create(Object result, String status) {

        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

}
