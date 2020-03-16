package com.xhgj.pay.servlet;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 周宇
 * @create 2020-03-16 14:29
 * 调用支付宝支付页面
 */
@WebServlet("/xhgj/pay01")
public class PayPage1 extends HttpServlet {

    // 应用标识
    private final String APP_ID = "2016092300576601";
    // 应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIiXZ9HdbL/Pdpm/yPfaSzDTff6fGC+Ku92cCagwG8oTvGG+a4i+l1d6DBq+7I6QFB8fMb4ZvEvvixTiamdGrVhDx6LMfr+c38YmRbM/T/WcU8ly7VxJ+L/w1nB9e2b/F0mfp9Di8hp/fqzMPkdm4woB0CMuo8LZSl2YlJW+OSKO/ade8qVmfhkQDUtWVfQbdB5EiQyDOh9R/TKFGm47jg/9tfuPYpkR4ZPFnH77TcLOWBvMCl8lLcPdIpCgbIouoF8Ez7FQ9iQfxQ6k8UjsM3jwmHpy3ZVBqBGmtrODDV6AMxkiGWsRq/uVoOYjkLqTmblgVXe66j58TnlpFT1BTtAgMBAAECggEADVIsB0T0Ae7qTJl1aKJ/gSCXBPeUIo0UZG9sRSbm8npglJGATx0HZ/iF7J9TqrJg9wgeCehXBDkFlnbjenX9PluqctkPoEw89pS66dMF4ivl/GVbomuOjWAS9KxlNV1yl0e2kqDhpEz4+SWFLzMGySO18NL622gvCodQx6F5cHb9L+K5bKvy6Op3pkeUjFFve9z0GxeRCcGsOAwm75ZwDqh30U+MMwuojJT4CPGsRdfB/PNW6auLsJkKaVA6cjB4gnMMLcHwTicdfSzTbfygjY7EjAlbg4tAxEJb9LnEhYzCSUBqi7wWbdHkmM9f2pO12Z4AKunn/oTlOIaYp49TQQKBgQDV4xTBa1EibopY3jjUITViinNup7qh0BeZ6wCSLgkOfFfJr4Bf4JPavom+uuHYDH26J3eyUeBoNYFZaO5+48kfEfExuwxDBYj6ZzkQiD5jIQOXSXSQghbyYi1Hcti8p9YuzdTSA9bS/n/0Unl9fKBkTbLnaopgS9z2HNWfkJENCQKBgQCja5Jz1gb4OOqbcPPEGBfeokiTH0bl7Pt43aaMJ4XhXGND+/9aXX6FPxdQvqwfSv+mzM3UZMmN6yvQ0Z5NlSOskVyb5gZ5PZjAZltMsFhw+fwwEZmHcbaJwHkF5gv3Nckgx+KiSP+CNpnzhNGQevP/IV8U66iPv4sttEKJ5izlxQKBgC/XWjYlTgGYniNgmW+BehJvi4BNziYRiKXp9be8ylPOJki7k8heUFQWWHMsFAYiTSENYO2m+L32cddQ7D+/raXgeoW4I/b9zvTLj+0NvH6pzMbTLFqCbqWKpjwcCTSxievwKFFNgIizWmWI6RnTfYDSDFlM8GS3s1Xtfy7wxDEZAoGAE6oID2Q7pnbC1D19N6VmieWhMgLbdyNTBzI0rjy7QWjqkO2rxmXlqYbz+UVYVNOje6C9S8ds8WTQ+umeUNuXVVaQfDnroX8l+FDYv5ugqMeL7wTPIpsuSdkdNvZ9jhusvgMPZFPAWxzS3Zin1ujUn2iWll/dAp8b0j4Um59U27kCgYEAk/W2DVPkTFyDrYUcP5MtnauY+ajK0Mc/xoMfdfvm/G5ec5a9dTVXBDcmxeUdXZfkPWswq1Iy5hl5pR51dietwv5JX/ANDdj2wxGV/KmwFPCIdkz4oz5zFqqh+15lqd0zQxDxlx3GRifYA6FQiE16iOlCiLaIbSge3MvfOM3iN5I=";
    // 编码格式
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2TvSLJkO5CLa6AxCCG+g9e8n5hm/71RgVtbh1A0hhtBUfOQ/W0gVeCvlsgR3PhZ71NOTsXxSEvpKM2GJb7dtf50wSFIU/ZZxKf67SmacVqOKs6Bt+HfSbSibh9Qr1xnd3VnHaF7raYPdhkb9jpJThDJjX1/5u1PU1vuSQIYZl8amaJUyt1cA9h5S2S2N2/mPW56eF7m0VWuODkx8zKnssgnzTvb6c/MNniNwfTsM5ou9UWzReEhDh8O2QWy+440DN7pSSSS2QYPw/fnWYT2jXvY7Lz6Zj+taiEE9DIMLt2uWS2d+fOlGWAFSnYiHHZFP18gl4peaV8EPVPVAXdmoMQIDAQAB";
    // 沙箱路径接口，正式路径应为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    // 参数返回格式
    private final String FORMAT = "JSON";
    // 签名方式
    private final String SIGN_TYPE = "RSA2";
    // 支付宝异步通知路径，付款完毕后会异步调用本项目的方法，必须为公网地址
    private final String NOTIFY_URL = "http://127.0.0.1/notifyURL";
    // 支付宝同步通知路径，也就是当付款完毕后跳转本项目的页面，可以不是公网地址
    private final String RETURN_URL = "http://127.0.0.1:8888/alipay/hxgj/pay02";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse httpResponse) throws ServletException, IOException {

        System.err.println("支付中......");

        //接收前台金额
        Integer monry = Integer.parseInt(req.getParameter("money"));

        // 实例化客户端,填入所需参数
        // 网关地址 应用标识 应用私钥 传输格式 编码格式 支付宝公钥 签名类型
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        //生成随机Id
        String out_trade_no = UUID.randomUUID().toString();
        //付款金额，必填
        String total_amount =Integer.toString(monry);
        //订单名称，必填
        String subject ="顶级主持人 家沐 金牌支持  quattro豪华型";
        //商品描述，可空
        String body = "尊敬的会员欢聘用 金牌支持  家沐 竭尽为您服";
        request.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

    }

}