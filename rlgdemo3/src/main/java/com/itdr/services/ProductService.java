package com.itdr.services;

import com.alipay.api.domain.Product;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface ProductService {
    /**
     * 获取商品分类信息
     */
    ServerResponse<Product> topCategory(Integer pid);

    /**
     * 获取商品详情
     * @param productId
     * @param is_new
     * @param is_hot
     * @param is_banner
     * @return
     */

    ServerResponse<Users> detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner);

    /**
     * 商品搜索 + 动态排序
     * @param productId
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<Users> listProduct(Integer productId, String keyWord, Integer pageNum, Integer pageSize, String orderBy);
}
