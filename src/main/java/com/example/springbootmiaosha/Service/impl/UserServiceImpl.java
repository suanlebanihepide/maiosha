/*
 * @Author: shenzheng
 * @Date: 2020/6/6 22:36
 */

package com.example.springbootmiaosha.Service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.springbootmiaosha.Service.UserService;
import com.example.springbootmiaosha.Service.model.UserModel;
import com.example.springbootmiaosha.dao.UserDOMapper;
import com.example.springbootmiaosha.dao.UserPasswordDOMapper;
import com.example.springbootmiaosha.dataobject.UserDO;
import com.example.springbootmiaosha.dataobject.UserPasswordDO;
import com.example.springbootmiaosha.error.BusinessException;
import com.example.springbootmiaosha.error.EmBusinessError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(int id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        UserPasswordDO passwordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromDataObject(userDO, passwordDO);
    }

    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName()) ||
                userModel.getAge() == null || userModel.getGender() == null
                || StringUtils.isEmpty(userModel.getTelphone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.TELPHONE_ERROR);
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {

        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException((EmBusinessError.LOGIN_ERROR));
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userMode= convertFromDataObject(userDO,userPasswordDO);

        if(!StringUtils.equals(password,userMode.getEncrptPassword()))
        {
            throw new BusinessException((EmBusinessError.LOGIN_ERROR));

        }
        return userMode;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel) {

        if (userModel == null) return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null || userPasswordDO == null) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
