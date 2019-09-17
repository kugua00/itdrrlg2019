package com.itdr.services.impl;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UserMapper;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import com.itdr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    UserMapper userMapper;


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
        int i = userMapper.selectByUserNameOrEmail(username, "username");
        if (i <= 0){
            return ServerResponse.defeateRS("用户名不存在");
        }

        //MD5加密
        String md5Password = MD5Utils.getMD5Code(password);


        //根据用户名和密码查询用户是否存在
        Users u = userMapper.selectByUsernameAndPassword(username,md5Password);
        if (u == null){
            return ServerResponse.defeateRS("账号或密码错误");
        }

        //封装数据并返回
        u.setPassword(""); //把密码置空返回
        ServerResponse sr = ServerResponse.successRS(u);
        return sr;
    }


    //用户注册
    @Override
    public ServerResponse<Users> register(Users u) {
        System.out.println("123");
        if (u.getUsername() == null || u.getUsername().equals("")){


            return ServerResponse.defeateRS("用户名不能为空");
        }

        if (u.getPassword() == null || u.getPassword().equals("")){
            return ServerResponse.defeateRS("密码不能为空");
        }


        //根据用户名查找是否存在该用户
        int i2 = userMapper.selectByUserNameOrEmail(u.getUsername(), "username");
        if (i2 > 0){
            return ServerResponse.defeateRS("用户名已存在");
        }

        //MD5加密
        u.setPassword(MD5Utils.getMD5Code(u.getPassword()));

        System.out.println(u.getUsername()+"++++" +u.getPassword());
        int i= userMapper.insert(u);
        if (i<=0){
            return ServerResponse.defeateRS("用户注册失败");
        }
        return ServerResponse.successRS(200,null,"注册成功");
    }


    //检查用户名或者邮箱是否存在
    @Override
    public ServerResponse<Users> checkUserName(String str, String type) {
        if (str == null || str.equals("")){
            return ServerResponse.defeateRS("参数不能为空");
        }
        if (type == null || type.equals("")){
            return ServerResponse.defeateRS("参数类型不能为空");
        }


        //检查注册用户是否存在
        int i = userMapper.selectByUserNameOrEmail(str, type);
        if (i>0 && type.equals("username")){
            return ServerResponse.defeateRS("用户名已经存在");
        }
        if (i>0 && type.equals("email")){
            return ServerResponse.defeateRS("邮箱已经存在");
        }
        return ServerResponse.successRS(200,null,"校验成功");

    }


    //获取用户详细信息
    @Override
    public ServerResponse getInforamtion(Users users){
        Users users1 = userMapper.selectByPrimaryKey(users.getId());
        if(users1 == null){
            return ServerResponse.defeateRS("用户不存在");
        }
        users1.setPassword("");
        return ServerResponse.successRS(users1);
    }


    //登录状态更新个人信息
    @Override
    public ServerResponse updata_Inforamtion(Users users) {
        int i1 = userMapper.selectByEmailAndId(users.getEmail(), users.getId());
        if (i1>0){
            return ServerResponse.defeateRS("要更新的邮箱已经存在");
        }

        int i = userMapper.updateByPrimaryKeySelective(users);
        if (i<=0){
            return ServerResponse.defeateRS("更新失败");
        }

        return ServerResponse.successRS("更新个人信息成功");
    }

    //忘记密码
    @Override
    public ServerResponse forgetGetQuestion(String username) {
        if (username == null || username.equals("")){
            return ServerResponse.defeateRS("参数不能为空");
        }
        int i = userMapper.selectByUserNameOrEmail(username, Const.USERNAME);
        if (i<=0){
            return ServerResponse.defeateRS("用户名不存在");
        }

        String question = userMapper.selectByUserName(username);
        if (question == null ||question.equals("")){
            return ServerResponse.defeateRS("该用户未设置安全问题");
        }

        return ServerResponse.successRS(question);
    }


    //提交问题答案
    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String anser) {
        //参数是否为空
        if(username == null ||  username.equals("")){
            return ServerResponse.defeateRS("用户名不能为空");
        }
        if(question == null ||  question.equals("")){
            return ServerResponse.defeateRS("问题不能为空");
        }
        if(anser == null ||  anser.equals("")){
            return ServerResponse.defeateRS("答案不能为空");
        }

        int i = userMapper.selectByUsernameAndQuestionAndAnswer(username, question, anser);
        if (i<=0){
            return ServerResponse.defeateRS("问题答案不匹配");
        }

        //产生随机字符令牌
        String token = UUID.randomUUID().toString();
        //将令牌放入缓存中，这里使用的是Google的guava缓存，后期会使用redis替代
        TokenCache.set("token_"+username,token);

        return ServerResponse.successRS(token);
    }


    //忘记密码重设密码
    @Override
    public ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(username == null ||  username.equals("")){
            return ServerResponse.defeateRS("用户名不能为空");
        }
        if(passwordNew == null ||  passwordNew.equals("")){
            return ServerResponse.defeateRS("新密码不能为空");
        }
        if(forgetToken == null ||  forgetToken.equals("")){
            return ServerResponse.defeateRS("非法的令牌参数");
        }

        //判断缓存中的token
        String s = TokenCache.get("token_" + username);
        if(s == null ||  s.equals("")){
            return ServerResponse.defeateRS("token过期了");
        }

        if(!s.equals(forgetToken)){
            return ServerResponse.defeateRS("非法的token");
        }

        //Md5加密
        String md5 = MD5Utils.getMD5Code(passwordNew);

        int i = userMapper.updateByUserNameAndPassword(username, md5);
        if (i<=0){
            return ServerResponse.defeateRS("修改密码失败");
        }

        return ServerResponse.successRS("修改密码成功");
    }

    //登录状态中重设密码
    @Override
    public ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew) {
        if(passwordOld == null ||  passwordOld.equals("")){
            return ServerResponse.defeateRS("参数不能为空");
        }
        if(passwordNew == null ||  passwordNew.equals("")){
            return ServerResponse.defeateRS("参数不能为空");
        }

        //Md5加密
        String md5 = MD5Utils.getMD5Code(passwordOld);


        int i = userMapper.selectByIdAndPassword(users.getId(), md5);
        if (i<=0){
            return ServerResponse.defeateRS("输入错误");
        }
        //Md5加密
        String md51 = MD5Utils.getMD5Code(passwordNew);


        int i1 = userMapper.updateByUserNameAndPassword(users.getUsername(), md51);
        if (i1 <=0){
            return ServerResponse.defeateRS("修改密码失败");
        }
        return ServerResponse.successRS("修改密码成功");
    }


}
