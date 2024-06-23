package top.kangyaocoding.training.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kangyaocoding.training.interceptor.LoginInterceptor;
import top.kangyaocoding.training.interceptor.RefreshTokenInterceptor;

import javax.annotation.Resource;

/**
 * 描述: 登录配置类
 *
 * @author K·Herbert
 * @since 2024-06-22 20:07
 */

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 配置拦截器。
     *
     * @param registry 拦截器注册表，用于添加和配置拦截器。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器，排除登录、获取验证码和更新接口的拦截
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login/**",
                        "/user/code",
                        "update/**"
                ).order(1);

        // 添加刷新令牌的拦截器，对所有请求进行拦截以检查令牌是否需要刷新
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(0);
    }
}

