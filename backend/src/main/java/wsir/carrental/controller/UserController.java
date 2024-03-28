package wsir.carrental.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.dict.UserType;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.UserDto;
import wsir.carrental.service.MailService;
import wsir.carrental.service.UserService;
import wsir.carrental.util.RedisUtil;
import wsir.carrental.util.Result;
import wsir.carrental.entity.vo.UserVo;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody UserVo userVo) {
        return Result.success(userService.login(userVo));
    }

    @PostMapping("/register")
    public <T> Result<T> register(@RequestBody UserVo userVo) {
        // 判断验证码是否合理
        switch (mailService.checkPassCode(userVo.getEmail(), userVo.getPassCode())) {
            case 0:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码已过期");
            case 1:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码错误");
        }

        //  已存在该用户
        if (ObjectUtil.isNotNull(userService.selectUserByEmailOrName(userVo))) {
            return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户已经被注册");
        }

        // 注册账号
        if (1 == userService.register(userVo)) {
            redisUtil.expire(userVo.getEmail(), -2);
            return Result.success();
        } else {
            return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "注册失败");
        }
    }

    @PostMapping("/chgPwd")
    public <T> Result<T> chgPwd(@RequestBody UserVo userVo) {
        // 判断验证码是否合理
        switch (mailService.checkPassCode(userVo.getEmail(), userVo.getPassCode())) {
            case 0:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码已过期");
            case 1:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码错误");
        }

        // 修改密码
        if (0 < userService.chgPwd(userVo)) {
            redisUtil.expire(userVo.getEmail(), -2);
            return Result.success();
        } else {
            return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "修改密码失败");
        }
    }

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String email,
                                                @RequestParam String userName,
                                                @RequestParam UserStatus status,
                                                @RequestParam UserType userType,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<User> pages = userService.getPages(email, userName, status, userType, current, size);
        return Result.success(Map.of("page", pages));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertWorkerOrAdmin(@RequestBody UserVo userVo) {
        if (userService.insertWorkerOrAdmin(userVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加员工或管理员失败！");
    }

    @PostMapping("/delMore")
    public <T> Result<T> deleteBatchWorkerOrAdmin(@RequestBody List<String> ids) {
        if (userService.deleteBatchWorkerOrAdmin(ids) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除员工或管理员失败！");
    }

    @PostMapping("/chgUser")
    public <T> Result<T> chgUser(@RequestBody UserVo userVo) {
        if (userService.chgUser(userVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "更改用户信息失败！");
    }

    @PostMapping("/chgUserByUser")
    public <T> Result<T> chgUserByUser(@RequestBody UserVo userVo) {
        // 判断验证码是否合理
        switch (mailService.checkPassCode(userVo.getEmail(), userVo.getPassCode())) {
            case 0:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码已过期");
            case 1:
                return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "验证码错误");
        }

        //  用户名或邮箱已被注册
        if (userService.selectUserByEmailOrNameChgInPersonal(userVo)) {
            return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户名或邮箱已被注册");
        }

        // 修改个人信息
        if (0 < userService.chgUser(userVo)) {
            redisUtil.expire(userVo.getEmail(), -2);
            return Result.success();
        } else {
            return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "更改用户信息失败");
        }
    }

    @PostMapping("/charge")
    public <T> Result<T> charge(@RequestBody UserVo userVo) {
        //  判断密码是否正确
        if (userService.checkPwdFail(userVo)) {
            return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户密码错误，充值失败");
        }

        userVo.setPassword(null);
        // 修改个人信息
        if (0 < userService.chgUser(userVo)) {
            return Result.success();
        } else {
            return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "充值操作失败");
        }
    }

    @GetMapping("/getOne")
    public Result<UserDto> getOne(@RequestParam String userId) {
        return Result.success(userService.getOne(userId));
    }

    @GetMapping("/getOneIdentityMsg")
    public Result<UserDto> getOneIdentityMsg(@RequestParam String userId) {
        return Result.success(userService.getOneIdentityMsg(userId));
    }

    @GetMapping("/getUserCount")
    public Result<Map<String, Long>> getuserCount() {
        return Result.success(userService.getUserCount());
    }
}
