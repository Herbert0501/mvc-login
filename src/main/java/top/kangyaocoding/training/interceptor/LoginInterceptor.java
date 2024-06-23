package top.kangyaocoding.training.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import top.kangyaocoding.training.utils.UserHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断是否需要拦截(ThreadLocal中是否有用户)
        if (UserHolder.getUser() == null) {
            //没有用户则拦截
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
