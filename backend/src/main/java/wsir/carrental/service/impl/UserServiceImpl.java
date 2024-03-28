package wsir.carrental.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.component.UserPrincipal;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.dict.UserType;
import wsir.carrental.entity.domain.Image;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.UserDto;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.mapper.UserMapper;
import wsir.carrental.service.UserService;
import wsir.carrental.util.JwtUtil;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPrincipal userPrincipal;

    @Value("${files.defaultUrl}")
    private String defaultUrl;

    @Override
    @Transactional
    public Map<String, Object> login(User user) {
        //  通过email登录用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getUserName());
        User userByEmail = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNotNull(userByEmail)) {
            user.setUserName(userByEmail.getUserName());
        }

        //  验证用户名和密码
        MPJLambdaWrapper<User> mpjLambdaWrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Image::getUrl, UserDto::getUrl)
                .leftJoin(Image.class, Image::getId, User::getImageId)
                .eq(User::getUserName, user.getUserName());
        UserDto loginUser = userMapper.selectJoinOne(UserDto.class, mpjLambdaWrapper);
        if (ObjectUtil.isNull(loginUser) || !BCrypt.checkpw(user.getPassword(), loginUser.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "用户名或密码错误");
        }
        if (loginUser.getStatus().equals(UserStatus.Block)) {
            throw new ServiceException(HttpURLConnection.HTTP_PROXY_AUTH, "该用户已被封禁，请及时和管理员联系");
        }

        // 认证通过，使用userId生成jwt
        String jwt = jwtUtil.createJWT(loginUser.getId());
        loginUser.setToken(jwt);

        //  赋值userPrincipal
        userPrincipal.setCurrentUser(loginUser);
        return Map.of("user_info", loginUser);
    }

    @Override
    @Transactional
    public Integer register(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(UserStatus.Normal);
        user.setUserType(UserType.Customer);
        user.setImageId(defaultUrl);
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public Integer chgPwd(User user) {
        User oldUser = this.selectUserByEmailOrName(user);
        if (ObjectUtil.isNull(oldUser)) {
            throw new ServiceException(HttpURLConnection.HTTP_PROXY_AUTH, "该用户不存在");
        }
        oldUser.setPassword(BCrypt.hashpw(user.getPassword()));
        return userMapper.updateById(oldUser);
    }

    @Override
    @Transactional
    public User selectUserByEmailOrName(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail()).or().eq(User::getUserName, user.getUserName());
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public Boolean selectUserByEmailOrNameChgInPersonal(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail()).or().eq(User::getUserName, user.getUserName());
        List<User> users = userMapper.selectList(wrapper);
        if (users.size() > 1) {
            return Boolean.TRUE;
        }
        if (users.get(0).getEmail().equals(userPrincipal.getCurrentUser().getEmail())
            && users.get(0).getUserName().equals(userPrincipal.getCurrentUser().getUserName())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean checkPwdFail(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, user.getEmail());
        User selectedUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(selectedUser) || !BCrypt.checkpw(user.getPassword(), selectedUser.getPassword())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public IPage<User> getPages(String email,
                                String userName,
                                UserStatus status,
                                UserType userType,
                                Long current,
                                Long size) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isBlank(email)) {
            wrapper.like(User::getEmail, email);
        }
        if (!StrUtil.isBlank(userName)) {
            wrapper.like(User::getUserName, userName);
        }
        if (!UserStatus.Wrong.equals(status)) {
            wrapper.eq(User::getStatus, status);
        }
        if (!UserType.Wrong.equals(userType)) {
            wrapper.eq(User::getUserType, userType);
        }

        Page<User> page = new Page<>(current, size);
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public Integer insertWorkerOrAdmin(User user) {
        if (ObjectUtil.isNull(selectUserByEmailOrName(user))) {
            return 0;
        }
        user.setCreateBy(null);
        user.setPassword(BCrypt.hashpw("123456"));
        user.setImageId(defaultUrl);
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public Integer deleteBatchWorkerOrAdmin(List<String> ids) {
        return userMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public Integer chgUser(User user) {
        user.setUpdateBy(null);
        user.setUpdateTime(null);
        return userMapper.updateById(user);
    }

    @Override
    @Transactional
    public UserDto getOne(String userId) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Image::getUrl, UserDto::getUrl)
                .leftJoin(Image.class, Image::getId, UserDto::getImageId)
                .eq(User::getId, userId);
        return userMapper.selectJoinOne(UserDto.class, wrapper);
    }

    @Override
    @Transactional
    public UserDto getOneIdentityMsg(String userId) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .select("image1.url", UserDto::getFrontSideUrl)
                .select("image2.url", UserDto::getReverseSideUrl)
                .leftJoin(Image.class, "image1", Image::getId, User::getFrontSideImage)
                .leftJoin(Image.class, "image2", Image::getId, User::getReverseSideImage)
                .eq(User::getId, userId);
        return userMapper.selectJoinOne(UserDto.class, wrapper);
    }

    @Override
    @Transactional
    public Map<String, Long> getUserCount() {
        Map<String, Long> result = new HashMap<>();
        result.put("total", userMapper.selectCount(null));
        result.put("customer", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUserType, UserType.Customer)));
        result.put("block", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getStatus, UserStatus.Block)));
        result.put("rentCar", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getRentCar, Boolean.TRUE)));
        return result;
    }
}
