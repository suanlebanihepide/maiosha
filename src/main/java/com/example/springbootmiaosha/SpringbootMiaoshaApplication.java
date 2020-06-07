package com.example.springbootmiaosha;

import com.example.springbootmiaosha.dao.UserDOMapper;
import com.example.springbootmiaosha.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(scanBasePackages ="com.example.springbootmiaosha" )
@MapperScan("com.example.springbootmiaosha.dao")
public class SpringbootMiaoshaApplication {


    public static void main(String[] args) {
        System.out.println("hello world");
        SpringApplication.run(SpringbootMiaoshaApplication.class, args);

    }

}
