package com.example.springbootmiaosha.Service;

import com.example.springbootmiaosha.Service.model.UserModel;
import com.example.springbootmiaosha.dataobject.UserDO;
import com.example.springbootmiaosha.error.BusinessException;

public interface UserService {
    UserModel getUserById(int id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone,String password) throws BusinessException;
}
