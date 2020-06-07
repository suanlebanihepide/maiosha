package com.example.springbootmiaosha.error;

import lombok.Data;


public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
