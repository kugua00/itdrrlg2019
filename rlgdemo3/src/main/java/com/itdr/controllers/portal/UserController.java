package com.itdr.controllers.portal;


import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@ResponseBody   //表示类中的请求返回的都是 json格式
@RequestMapping("/user/")

public class UserController {

    @Autowired
    UserService userService;

    //用户登录
    @PostMapping("login.do")
    public ServerResponse<Users> logindo(String username, String password, HttpSession session ){
        ServerResponse<Users> sr = userService.login(username,password);

        //当返回的是成功状态，才执行
        if (sr.isSuccess()) {
            Users u = sr.getData();
            session.setAttribute(Const.LOGINUSER,u);

            Users u2 = new Users();
            u2.setId(u.getId());
            u2.setUsername(u.getUsername());
            u2.setEmail(u.getEmail());
            u2.setPhone(u.getPhone());
            u2.setCreateTime(u.getCreateTime());
            u2.setUpdateTime(u.getUpdateTime());
            u2.setPassword("");

            sr.setData(u2);
        }
        return sr;
    }


    //用户注册
    @PostMapping("register.do")
    public ServerResponse<Users> register(Users u ){
        System.out.println("123456");
        ServerResponse<Users> sr = userService.register(u);
        return sr;
    }

    //检查用户或邮箱是否存在
    @PostMapping("check_valid.do")
    public ServerResponse<Users> checkUserName(String str, String type){
        ServerResponse<Users> sr = userService.checkUserName(str,type);
        return sr;
    }

    //获取用户登录信息
    @PostMapping("get_user_info.do")
    public ServerResponse<Users> getUserInfo(HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeateRS(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return ServerResponse.successRS(users);
        }
    }


    //退出登录
    @PostMapping("logout.do")
    public ServerResponse<Users> logout(HttpSession session){
       session.removeAttribute(Const.LOGINUSER);
        return ServerResponse.successRS("退出成功");
    }



    //获取当前登录用户详细信息
    @PostMapping("get_inforamtion.do")
    public ServerResponse<Users> getinforamtion(HttpSession session){
        Users users = (Users)session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeateRS(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else{
            ServerResponse sr = userService.getInforamtion(users);
            return sr;
        }

    }




    //登录状态更新个人信息
    @PostMapping("updata_inforamtion.do")
    public ServerResponse<Users> updata_inforamtion(Users u,HttpSession session){
        Users users = (Users)session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeateRS(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else{
            u.setId(users.getId());
            u.setUsername(users.getUsername());
            ServerResponse sr = userService.updata_Inforamtion(u);
            session.setAttribute(Const.LOGINUSER,u);
            return sr;
        }

    }

    //忘记密码
    @PostMapping("forget_get_question.do")
    public ServerResponse<Users> forgetGetQuestion(String username ){
        return  userService.forgetGetQuestion(username);

    }

    //提交问题答案
    @PostMapping("forget_check_answer.do")
    public ServerResponse<Users> forgetCheckAnswer(String username, String question , String answer){
        return  userService.forgetCheckAnswer(username,question,answer);
    }


    //忘记密码的重设密码
    @PostMapping("forget_reset_password.do")
    public ServerResponse<Users> forgetResetPassword(String username, String passwordNew , String forgetToken){
        return  userService.forgetResetPassword(username,passwordNew,forgetToken);
    }



    //登录状态重置密码
    @PostMapping("reset_password.do")
    public ServerResponse<Users> resetPassword(String passwordOld ,String passwordNew , HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeateRS("用户未登录");
        }else {
            return  userService.resetPassword(users,passwordOld,passwordNew);

        }


    }
}
