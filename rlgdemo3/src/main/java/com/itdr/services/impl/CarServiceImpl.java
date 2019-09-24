package com.itdr.services.impl;


import com.itdr.common.Const.Cart;
import com.itdr.common.ServerResponse;
import com.itdr.services.CartService;
import com.itdr.mappers.CartsMapper;
import com.itdr.mappers.ProductsMapper;
import com.itdr.pojo.Carts;
import com.itdr.pojo.Products;
import com.itdr.pojo.vo.CartProductVO;
import com.itdr.pojo.vo.CartVO;
import com.itdr.utils.BigDecimalUtils;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CarServiceImpl implements CartService {

    @Autowired
    CartsMapper cartsMapper;

    @Autowired
    ProductsMapper productsMapper;



     //购物车添加商品
     @Override
    public ServerResponse<CartVO> addOne(Integer productId, Integer count, Integer uid){
        //参数非空判断
        if (productId == null || productId <=0 || count <= 0){
            return ServerResponse.defeateRS("非法参数");
        }

/*        //向购物车表中存储数据
        //创建一个Cart对象
        Carts carts = new Carts();
        carts.setUserId(uid);
        carts.setProductId(productId);
        carts.setQuantity(count);*/

        //如果有这条购物信息，就是更新购物数量，如果没有就插入新数据
        Carts cnt = cartsMapper.selectByUidAndProductID(uid,productId);

        if (cnt!= null){
            //更新数据
            cnt.setQuantity(cnt.getQuantity() + count);
            int i = cartsMapper.updateByPrimaryKeySelective(cnt);
        }else {
            //插入新数据
            //创建一个Cart对象
            Carts carts = new Carts();
            carts.setUserId(uid);
            carts.setProductId(productId);
            carts.setQuantity(count);

            int insert  = cartsMapper.insert(carts);
        }
/*
        CartVO cartVO = getCartVo(uid);
        return ServerResponse.successRS(cartVO);*/
        return listCart(uid);
    }



    //购物车高可复用方法
    private CartVO getCartVo(Integer uid){
         //创建CartVo对象
        CartVO cartVO = new CartVO();

        //创建变量存储购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

         //用来存放CartProductVO对象的集合
        List<CartProductVO> cartProductVOList = new ArrayList<CartProductVO>();


        //根据用户ID查询该用户的所有购物车信息
        List<Carts> liCart = cartsMapper.selectByUid(uid);

        //从购物信息集合中拿出每一条数据，根据其中的商品ID查询需要的商品信息
        if (liCart.size() != 0) {
            for (Carts cart : liCart
            ) {
                //根据购物信息中的商品ID查询商品数据
                Products p =  productsMapper.selectByID(cart.getProductId(),0,0,0);

                //使用工具类进行数据封装
                CartProductVO cartProductVO = PoToVoUtil.getOne(cart, p);

                //购物车更新有效库存
                Carts cartForQuantity= new Carts();
                cartForQuantity.setId(cart.getId());
                cartForQuantity.setQuantity(cartProductVO.getQuantity());
                cartsMapper.updateByPrimaryKeySelective(cartForQuantity);


                //计算购物车总价
                if (cart.getChecked() == Cart.CHECK){

                     cartTotalPrice =  BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }



            /*    //封装cartProductVo
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setProductId(cart.getProductId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setProductChecked(cart.getChecked());

                Products p =  productsMapper.selectByID(cart.getProductId(),0,0,0);

                //查询到的商品不能为null
                if ( p != null){
                    cartProductVO.setName(p.getName());
                    cartProductVO.setSubtitle(p.getSubtitle());
                    cartProductVO.setMainImage(p.getMainImage());
                    cartProductVO.setPrice(p.getPrice());
                    cartProductVO.setStock(p.getStock());
                    cartProductVO.setStatus(p.getStatus());
                }


                //计算本条购物信息总价
                BigDecimal productTotalPrice = BigDecimalUtils.mul(p.getPrice().doubleValue(),cart.getQuantity());
                cartProductVO.setProductTotalPrice(productTotalPrice);

                Integer count = 0;

                //处理库存有问题
                if(cart.getQuantity() <= p.getStock()){
                    count = cart.getQuantity();
                    cartProductVO.setLimitQuantity(Const.Cart.LIMITQUANTITYSUCCESS);
                }else{
                    count = p.getStock();
                    cartProductVO.setLimitQuantity(Const.Cart.LIMITQUANTITYFAILED);
                }
                cartProductVO.setQuantity(count);
*/

                //把对象放到集合中
                cartProductVOList.add(cartProductVO);
            }
        }else{

        }

        //封装CartVO数据
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(this.checkAll(uid));
        cartVO.setCartTotalPrice(cartTotalPrice);

        try {
            cartVO.setImageHost(PropertiesUtil.getValue("imageHost"));
        }catch (IOException e){
            e.printStackTrace();
        }

        return cartVO;
    }


    /**
     * 判断用户购物车是否全选
     */
    private boolean checkAll (Integer uid){
        int i = cartsMapper.selectByUidCheck(uid, Cart.UNCHECK);

        if (i == 0){
            return true;
        }else {
            return false;
        }
    }


    //获取登录用户的购物车列表
    @Override
    public ServerResponse<CartVO> listCart(Integer id) {
        CartVO cartVO = this.getCartVo(id);
        return ServerResponse.successRS(cartVO);
    }



    //购物车更新商品
    @Override
    public ServerResponse<CartVO> updateCart(Integer productId, Integer count, Integer id) {
        //参数非空判断
        if (productId == null || productId <=0 || count <= 0){
            return ServerResponse.defeateRS("非法参数");
        }


        //如果有这条购物信息，就是更新购物数量，如果没有就插入新数据
        Carts cnt = cartsMapper.selectByUidAndProductID(id,productId);

        cnt.setQuantity(count);
        int i = cartsMapper.updateByPrimaryKeySelective(cnt);


        return listCart(id);
    }


    //购物车删除商品
    @Override
    public ServerResponse<CartVO> deleteCart(String productIds, Integer id) {
        if (productIds == null || "".equals(productIds)){
            return ServerResponse.defeateRS("非法参数");
        }

        //把字符串中的数据放到集合中
        String[] split = productIds.split(",");
        List<String> strings = Arrays.asList(split);


        int i = cartsMapper.deleteByProducts(strings,id);

        return listCart(id);
    }


     //查询在购物车里的商品信息条数
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer id) {
        List<Carts> carts = cartsMapper.selectByUid(id);
        return ServerResponse.successRS(carts.size());
    }


    //改变购物车中商品选中状态
    @Override
    public ServerResponse<CartVO> selectOrUnSelect(Integer id ,Integer check , Integer productId) {
        int  i = cartsMapper.selectOrUnSelect(id,check,productId);
        return listCart(id);
    }


}
