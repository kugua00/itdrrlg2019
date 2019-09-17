package com.itdr.mappers;

import com.itdr.pojo.Shippings;

public interface ShippingsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shippings record);

    int insertSelective(Shippings record);

    Shippings selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shippings record);

    int updateByPrimaryKey(Shippings record);
}