package com.itdr.controllers.portal;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.RequestWrapper;

@Controller
@ResponseBody
@RequestMapping("/user/")
public class UserController {


    @Autowired
    UserService userService;

    //用户登录
    @PostMapping("login.do")
    public ServerResponse<Users> logindo(String username, String password){
        ServerResponse<Users> sr = userService.login(username,password);
        Users u = sr.getData();
        session.setAttribute("user",u);


        Users u2 = new Users();
        u2.setId(u.getId());
        u2.setUsername(u.getUsername());
        u2.setEmail(u.getEmail());
        u2.setPhone(u.getPhone());
        u2.setCreateTime(u.getCreateTime());
        u2.setUpdateTime(u.getUpdateTime());
        u2.setPassword("");

        sr.setData(u2);
        return sr;
    }
}
