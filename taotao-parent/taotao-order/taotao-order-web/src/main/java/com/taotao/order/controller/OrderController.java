package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.IUserLoginService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单处理
 * @author Andre930
 * @create 2019-01-13 16:19
 */
@Controller
public class OrderController {

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    /**
     * url /order/order-cart
     * 参数  没有参数 但需要用户的id  从cookie中获取token  调用sso的服务获取
     * 返回值  逻辑视图   订单的确认页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response){

        /**
         * 1.从cookie中 获取用户的token
         * 2.调用sso的服务 获取用户的信息
         * 3.必须是用户登录了才展示
         * 4.展示用户的送货地址  根据用户的id查询该用户的配送地址   静态资源
         * 5.展示支付方式 从数据库中获取  静态资源
         * 6.调用购物车服务从redis数据库中获取购物车的商品列表
         * 7.将列表展示到页面中  》》》传递到页面  model
         */

        System.out.println(request.getAttribute("USER_INFO"));
        //获取用户的token
        /*String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        if(StringUtils.isNotBlank(token)){
            //调用sso服务 获取用户的信息
            TaotaoResult result = loginService.getUserByToken(token);
            if (result.getStatus()==200){
                //用户登录成功
                //调用cartservice服务从数据库中取购物车的数据
                TbUser  user= (TbUser) result.getData();
                List<TbItem> cartList = cartService.getCartList(user.getId());
                //将数据传递到 页面中
                request.setAttribute("cartList",cartList);

            }

        }
        */

        //从 request域中获取 用户信息
        TbUser  user= (TbUser) request.getAttribute("USER_INFO");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        //将数据传递到 页面中
        request.setAttribute("cartList",cartList);

        return "order-cart";
    }

    //创建订单

    /**
     * url: /order/create
     * 参数：表单使用 orderinfo 来接受
     * 返回值：逻辑视图
     * @param info
     * @return
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo info, HttpServletRequest request){

        //查询用户信息  设置到info属性中
        TbUser user_info = (TbUser) request.getAttribute("USER_INFO");
        info.setUserId(user_info.getId());
        info.setBuyerNick(user_info.getUsername());
        TaotaoResult result = orderService.createOrder(info);
        //向页面传递数据
        request.setAttribute("orderId",result.getData());
        request.setAttribute("payment",info.getPayment());
        DateTime dateTime = new DateTime();
        DateTime plusDays = dateTime.plusDays(3);//加3天
        request.setAttribute("date",plusDays.toString("yyyy-MM-dd"));

        return "success";
    }


}
