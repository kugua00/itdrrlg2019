package com.itdr.controllers.portal;

import com.alipay.api.domain.Product;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    ProductService productService;


    //获取商品分类
    @PostMapping("topcategory.do")
    public ServerResponse<Product> topCategoryDo(Integer pid){

        return productService.topCategory(pid);
    }

    //获取商品详情
    @PostMapping("detail.do")
    public ServerResponse<Users> detailDo(Integer productId,
                                            @RequestParam(value = "is_new",required = false,defaultValue = "0")Integer is_new,
                                            @RequestParam(value = "is_hot",required = false,defaultValue = "0")Integer is_hot,
                                            @RequestParam(value = "is_banner",required = false,defaultValue = "0")Integer is_banner
                                            ){

        return productService.detail(productId,is_new,is_hot,is_banner);
    }

    //商品搜索 + 动态排序
    @PostMapping("list.do")
    public ServerResponse<Users> listProduct(Integer productId,String keyWord,
                                             @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                             @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                             @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){

        return productService.listProduct(productId,keyWord,pageNum,pageSize,orderBy);
    }
}
