package wsir.carrental.component;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wsir.carrental.entity.domain.User;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.util.JwtUtil;
import wsir.carrental.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;

@Component
public class JwtAuthenticationTokenFilter implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPrincipal userPrincipal;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //  获取用户信息user_info
        UserVo userVo = JSON.parseObject(request.getHeader("user_info"), UserVo.class);
        if (ObjectUtil.isNull(userVo)) {
            throw new ServiceException(HttpURLConnection.HTTP_UNAUTHORIZED, "无token，请重新登录");
        }

        //  解析token
        String token = userVo.getToken();
        String userId;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new ServiceException(HttpURLConnection.HTTP_UNAUTHORIZED, "token非法或已失效，请重新登录");
        }
        User currentUser = userPrincipal.getCurrentUser();

        if (ObjectUtil.isNull(currentUser)) {
            throw new ServiceException(HttpURLConnection.HTTP_UNAUTHORIZED, "当前用户不存在");
        }

        if (!(userId.equals(currentUser.getId())
                && currentUser.getId().equals(userVo.getId())
                && currentUser.getEmail().equals(userVo.getEmail())
                && currentUser.getUserName().equals(userVo.getUserName()))) {
            throw new ServiceException(HttpURLConnection.HTTP_UNAUTHORIZED, "用户信息被篡改");
        }

        //  放行
        return true;
    }
}
