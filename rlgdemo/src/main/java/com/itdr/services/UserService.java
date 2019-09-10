package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface UserService {

    ServerResponse<Users>  login(String username,String password);
}
