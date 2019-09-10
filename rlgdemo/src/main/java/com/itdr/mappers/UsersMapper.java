package com.itdr.mappers;

import com.itdr.pojo.Users;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}