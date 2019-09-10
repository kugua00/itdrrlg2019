package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.mappers.UsersMapper;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {


    @Autowired
    UsersMapper usersMapper;


    //用户登录
    @Override
    public ServerResponse<Users> login(String username, String password) {

        if (username == null || username.equals("")){
            return ServerResponse.defeateRS("用户名不能为空");
        }

        if (password == null || password.equals("")){
            return ServerResponse.defeateRS("密码不能为空");
        }


        //根据用户名查找是否存在该用户

        //根据用户名和密码查询用户是否存在
        Users u = usersMapper.selectByUsernameAndPassword(username,password);

        if (u == null){
            return ServerResponse.defeateRS("账号或密码错误");
        }






        //封装数据并返回
        ServerResponse sr = ServerResponse.successRS(u);
        return sr;
    }



}
