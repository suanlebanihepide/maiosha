/*
 * @Author: shenzheng
 * @Date: 2020/6/6 22:35
 */

package com.example.springbootmiaosha.Controller;

import com.alibaba.druid.util.StringUtils;
import com.example.springbootmiaosha.Controller.response.CommonReturnType;
import com.example.springbootmiaosha.Controller.vo.UserVo;
import com.example.springbootmiaosha.Service.UserService;
import com.example.springbootmiaosha.Service.model.UserModel;
import com.example.springbootmiaosha.dataobject.UserPasswordDO;
import com.example.springbootmiaosha.error.BusinessException;
import com.example.springbootmiaosha.error.EmBusinessError;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //用户登录接口
    @RequestMapping(value = "/login",method = RequestMethod.POST,consumes = {CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone, @RequestParam(name = "password")String password,HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password))
        {
            throw  new BusinessException(EmBusinessError.LOGIN_ERROR);
        }


        //登录用户合法性
        UserModel userModel = userService.validateLogin(telphone,this.EncodeByMD5(password));

        System.out.println("登陆成功");
        //加入凭证
        request.getSession().setAttribute("IS_LOGIN",true);
        request.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);

    }


    //用户注册接口
    @RequestMapping(value = "/register",method = RequestMethod.POST,consumes = {CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "password")String password,
                                     @RequestParam(name = "gender")String gender,
                                     @RequestParam(name = "age")Integer age,
                                    HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和otpCode的一致性
//        String sessionOtpCode = (String) request.getSession().getAttribute(telphone);
//        if(StringUtils.equals(sessionOtpCode,otpCode)){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setAge(age);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(EncodeByMD5(password));
        try {
            userService.register(userModel);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        //用户注册
        return CommonReturnType.create(null);
    }
    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return  newstr;
    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = RequestMethod.POST,consumes = {CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone,HttpServletRequest request) {

        //生成OTP验证码
        Random random = new Random();
        int radomInt = random.nextInt(99999);
        radomInt+=100000;
        String otpCode = String.valueOf(radomInt);
        //将otp验证码同对应用户的手机号关联,使用httpsession后边redis
        request.getSession().setAttribute(telphone,otpCode);
        //短信通知给用户
        System.out.println("telphone = "+telphone+"otp code:"+otpCode);
        return CommonReturnType.create(null);
    }




    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserVo vo = convertFromModel(userService.getUserById(id));
        if (vo == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        return new CommonReturnType("success", vo);
    }

    private UserVo convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;
    }


}
