package com.itdr.services.impl;

import com.alipay.api.domain.Product;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategorysMapper;
import com.itdr.mappers.ProductsMapper;
import com.itdr.pojo.Categorys;
import com.itdr.pojo.Products;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.services.ProductService;
import com.itdr.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    ProductsMapper productsMapper;

    @Autowired
    CategorysMapper categorysMapper;



    /**
     * 获取商品分类信息
     * @param pid
     * @return
     */
    @Override
    public ServerResponse<Product> topCategory(Integer pid) {
        if (pid == null || pid < 0){
            return ServerResponse.defeateRS("非法的参数");
        }

        //根据商品分类ID查询子分类
        List<Categorys> li = categorysMapper.selectByParentId(pid);

        if (li == null){
            return ServerResponse.defeateRS("查询的ID不存在");
        }
        if (li.size() == 0){
            return ServerResponse.defeateRS("没有子分类");
        }

        return ServerResponse.successRS(li);
    }







    /**
     * 获取商品详情
     */
    @Override
    public ServerResponse<Users> detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner) {
        if (productId == null || productId < 0){
            return ServerResponse.defeateRS("非法的参数");
        }

        Products p = productsMapper.selectByID(productId,is_new,is_hot,is_banner);

        if (p == null){
            return ServerResponse.defeateRS("商品不存在");
        }


        ProductVO productVO = null;
        try {
            productVO = PoToVoUtil.productToProductVo(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ServerResponse.successRS(productVO);
    }


    /**
     * 商品搜索 + 动态排序
     * @param productId
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<Users> listProduct(Integer productId, String keyWord, Integer pageNum, Integer pageSize, String orderBy) {
        if ((productId == null  || productId < 0) && (keyWord == null || keyWord.equals(""))){
             return ServerResponse.defeateRS("非法的参数");
        }


        //分割排序参数
        String[] s = null;
        if(!"".equals(orderBy)){
             s = orderBy.split("_");
        }

        String keys = "%" + keyWord + "%";
        PageHelper.startPage(pageNum,pageSize);


        //根据ID或者名称查询商品
        List<Products>   li = productsMapper.selectByIdOrName(productId,keys,s[0],s[1]);

        PageInfo pf = new PageInfo(li);

        return ServerResponse.successRS(pf);
    }


}
