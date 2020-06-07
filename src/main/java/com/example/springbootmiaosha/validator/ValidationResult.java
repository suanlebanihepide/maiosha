/*
 * @Author: shenzheng
 * @Date: 2020/6/7 1:50
 */

package com.example.springbootmiaosha.validator;
import lombok.Data;

import java.util.Map;

@Data
public class ValidationResult {

    //校验结果
    private boolean hasErrors;
    private Map<String,String> errorMsgMap;

    //实现通用的格式化字符串信息
    public String getErrMsg(){

        return errorMsgMap.values().toArray().toString();
    }
}
