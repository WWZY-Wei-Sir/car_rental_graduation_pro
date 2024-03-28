package wsir.carrental;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.dict.UserType;
import wsir.carrental.entity.domain.*;
import wsir.carrental.entity.dto.CarDto;
import wsir.carrental.mapper.*;
import wsir.carrental.service.CarConfigService;
import wsir.carrental.service.ChatRecordService;
import wsir.carrental.service.ImageService;
import wsir.carrental.service.impl.UserServiceImpl;
import wsir.carrental.util.JwtUtil;

import java.util.*;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private CarTypeMapper carTypeMapper;

    @Autowired
    private CarNameMapper carNameMapper;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CarConfigMapper carConfigMapper;

    @Autowired
    private CarConfigService carConfigService;

    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CompanyFundingMapper companyFundingMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
        User user = new User();
        user.setEmail("qwer@asdasd.com");
        user.setStatus(UserStatus.Normal);
        user.setUserType(UserType.Admin);
        user.setUserName("hellozxczxc123");
        System.out.println(user.toString());
        System.out.println(user.isNew());

        userMapper.insert(user);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, "hello123");
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        System.out.println("users.size() = " + users.size());
        for (User user1 : users) {
            System.out.println(user1);
            System.out.println(user1.isNew());
        }
        IPage<User> userPage = new Page<>(3, 4);
        IPage<User> iPage = userMapper.selectPage(userPage, null);

        System.out.println("=============");
        for (User record : iPage.getRecords()) {
            System.out.println(record);
        }
    }

    @Test
    void test2() {
        System.out.println(UUID.randomUUID().toString().replace("-", "="));
    }

    @Test
    void test3() throws Exception {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, "hellozxc");
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        System.out.println(user);
        System.out.println("=====================");

        String pwd = BCrypt.hashpw("123456");
        System.out.println("passwordEncoder.encode(\"123456\") = " + pwd);
        user.setPassword(pwd);
        System.out.println(user);
        System.out.println("----------------------");
        Claims claims = jwtUtil.parseJWT(jwtUtil.createJWT(user.toString()));

        System.out.println("```````````````````````````````````````");
        System.out.println(claims.getSubject());
        System.out.println("```````````````````````````````````````");

        userMapper.updateById(user);
        System.out.println(BCrypt.checkpw("123456", user.getPassword()));
    }

    @Test
    void test4() {
//        Date a = DateUtil.date();
//        Date b = DateUtil.nextWeek();
//        Long starTime= a.getTime();
//        Long endTime= b.getTime();
//        Long num=endTime-starTime;//时间戳相差的毫秒数
//        System.out.println("相差天数为："+num/24/60/60/1000);//除以一天的毫秒数
        System.out.println(DateUtil.date().toJdkDate());
        System.out.println(DateUtil.date().toSqlDate());

        List<CompanyFunding> list = companyFundingMapper.selectList(new LambdaQueryWrapper<CompanyFunding>().orderByDesc(CompanyFunding::getDate));
        System.out.println(list);
    }
}
