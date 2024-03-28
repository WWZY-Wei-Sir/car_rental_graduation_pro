package wsir.carrental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wsir.carrental.component.JwtAuthenticationTokenFilter;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationTokenFilter)
                .addPathPatterns("/**") //拦截所有请求，判断token是否合法
                .excludePathPatterns("/**"); //开放接口
    }
}
