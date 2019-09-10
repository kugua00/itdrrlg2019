package com.itdr.mappers;

import com.itdr.pojo.Products;
import com.itdr.pojo.ProductsWithBLOBs;

public interface ProductsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductsWithBLOBs record);

    int insertSelective(ProductsWithBLOBs record);

    ProductsWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProductsWithBLOBs record);

    int updateByPrimaryKey(Products record);
}