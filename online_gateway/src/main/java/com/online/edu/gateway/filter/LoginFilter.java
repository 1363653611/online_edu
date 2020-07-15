package com.online.edu.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @Author : Brave
 * @Version : 1.0
 * @Date : 2020/2/12 15:20
 */
@Component
public class LoginFilter extends ZuulFilter {
    //定义过滤器类型
    @Override
    public String filterType() {
        return PRE_TYPE;
    }
    //过滤器执行顺序，返回值越小，越先执行
    @Override
    public int filterOrder() {
        return 1;
    }
    //返回false放行(不执行run()方法)，ture则不放行（执行run()方法）
    @Override
    public boolean shouldFilter() {
        //判断访问路径是否包含/vidservice/vod/getPlayAuth 进行登录校验
        //1、获取请求路径，uri，httpServletRequest
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();

        //当发生跨域请求时，浏览器会发起两次请求，第一次为跨域请求，其请求方法为"OPTIONS"，
        // 第二次为真实请求。当第一次请求成功后才会发起第二次请求。当项目中有拦截器时，由于第一次跨域请求并没有设置通过拦截器设置的条件，所以会被拦截器拦截导致浏览器不会向服务器发起真实请求
        String method = request.getMethod();
        if(method.equals("OPTIONS")){
            return false;
        }

        //2、根据获取请求路径判断，路径里是否包含 /eduvod/vid/getPlayAuth
        String playUrl = "/eduvod/vid/getPlayAuth";
        if(StringUtils.isNotEmpty(requestURI) && requestURI.contains(playUrl)){
            //3、如果包含，则放行
            return true;
        }
        return false;
    }

    //写过滤器的具体逻辑
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            //如果token为空，则不能访问接口
            //不会向后执行
            currentContext.setSendZuulResponse(false);
            //设置不能访问的状态码
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
