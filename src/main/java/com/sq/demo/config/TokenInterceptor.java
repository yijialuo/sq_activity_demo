package com.sq.demo.config;

import com.sq.demo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;


    /*
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String authorization = request.getHeader("authorization");

        if(authorization==null||authorization.isEmpty()){
            authorization=request.getParameter("authorization");
        }

        //没有token验证不通过
        if (authorization == null) {
            response.setStatus(401);
            return false;
        }
        String userId = authorization.split("=")[0];
        if (userId == null)
            return false;
        //查不到该条token或者token不对
        if (redisUtil.get(userId) == null || !redisUtil.get(userId).toString().equals(authorization)) {
            response.setStatus(401);
            return false;
        }
        //对的话重新设置过期时间
        redisUtil.set(userId, authorization, 172800);
        return true;
    }
}
