package com.itdr.mappers;

import com.itdr.pojo.Categorys;
import com.itdr.pojo.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Products record);

    int insertSelective(Products record);

    Products selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Products record);

    int updateByPrimaryKeyWithBLOBs(Products record);

    int updateByPrimaryKey(Products record);

    //根据商品ID获取商品详情
    Products selectByID(@Param("productId")Integer productId,
                        @Param("is_new")Integer is_new,
                        @Param("is_hot")Integer is_hot,
                        @Param("is_banner")Integer is_banner);


    //根据商品ID或者商品名称关键字查询数据
    List<Products> selectByIdOrName(@Param("productId")Integer productId,
                                    @Param("keyWord")String keyWord,
                                    @Param("s")String s,
                                    @Param("s1")String s1);

}