package com.xhgj.pay.servlet;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 周宇
 * @create 2020-03-16 15:24
 * 支付成功后返回给商家的同步回调方法
 */
@WebServlet("/hxgj/pay02")
public class PayPage2 extends HttpServlet {

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
    private final String RETURN_URL = "http://127.0.0.1/returnURL";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        System.err.println("同步回调方法......");

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println(params);//查看参数都有哪些
        boolean signVerified = false; // 调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //验证签名通过
        if(signVerified){

            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("商户订单号="+out_trade_no);
            System.out.println("支付宝交易号="+trade_no);
            System.out.println("付款金额="+total_amount);
            //支付成功，修复支付状态
            /*payService.updateById(Integer.valueOf(out_trade_no));*/
            System.out.println("ok");//跳转付款成功页面
        }else{
            System.out.println("no");//跳转付款失败页面
        }

    }
}