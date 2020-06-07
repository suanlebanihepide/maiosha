package com.example.springbootmiaosha.error;

public enum EmBusinessError implements CommonError {
    //通用错误类型000001

    PARAMETER_VALIDATION_ERROR(000001, "参数不合法"),
    UNKNOW_ERROR(000002, "参数不合法"),
    TELPHONE_ERROR(000003, "手机号码重复"),
    LOGIN_ERROR(000004, "用户手机号或者密码错误"),
    //30000开头为交易系信息
    STOCK_NOT_ENOUGH(300001, "库存不足"),
    //10000开头为用户相关错误定义
    USER_NOT_EXIST(10001,"用户不存在");


    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {

        this.errMsg =errMsg;
        return this;
    }
}
