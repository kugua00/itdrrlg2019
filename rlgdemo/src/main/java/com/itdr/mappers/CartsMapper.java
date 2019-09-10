package com.itdr.mappers;

import com.itdr.pojo.Carts;


public interface CartsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carts record);

    int insertSelective(Carts record);

    Carts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Carts record);

    int updateByPrimaryKey(Carts record);
}