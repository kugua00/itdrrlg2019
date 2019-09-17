package com.itdr.mappers;

import com.itdr.pojo.Products;


public interface ProductsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Products record);

    int insertSelective(Products record);

    Products selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Products record);

    int updateByPrimaryKeyWithBLOBs(Products record);

    int updateByPrimaryKey(Products record);


}