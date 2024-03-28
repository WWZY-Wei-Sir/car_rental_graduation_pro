package wsir.carrental.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private Integer JWT_TTL;  // 60 * 60 一个小时
    private String JWT_KEY;  //  设置秘钥明文

    public String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    public String createJWT(String subject, Integer ttlSecond) {
        JwtBuilder builder = getJwtBuilder(subject, ttlSecond, getUUID());// 设置过期时间
        return builder.compact();
    }

    public String createJWT(String id, String subject, Integer ttlSecond) {
        JwtBuilder builder = getJwtBuilder(subject, ttlSecond, id);// 设置过期时间
        return builder.compact();
    }

    private JwtBuilder getJwtBuilder(String subject, Integer ttlSecond, String id) {
        SecretKey secretKey = generalKey();

        Date nowTime = new Date();
        if(ObjectUtil.isNull(ttlSecond)){
            ttlSecond = this.JWT_TTL;
        }
        Date expDate = DateUtil.offsetSecond(nowTime, ttlSecond);
        System.out.println(nowTime);
        System.out.println(expDate);

        return Jwts.builder()
                .setId(id)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("car_rental_system")     // 签发者
                .setIssuedAt(nowTime)      // 签发时间
                .signWith(secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    //  生成加密后的秘钥 secretKey
    public SecretKey generalKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.JWT_KEY));
    }

    //  解析
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}

