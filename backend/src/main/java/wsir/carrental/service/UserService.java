package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.dict.UserType;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(User user);

    Integer register(User user);

    Integer chgPwd(User user);

    User selectUserByEmailOrName(User user);

    Boolean selectUserByEmailOrNameChgInPersonal(User user);

    Boolean checkPwdFail(User user);

    IPage<User> getPages(String email, String userName, UserStatus status, UserType userType, Long current, Long size);

    Integer insertWorkerOrAdmin(User user);

    Integer deleteBatchWorkerOrAdmin(List<String> ids);

    Integer chgUser(User user);

    UserDto getOne(String userId);

    UserDto getOneIdentityMsg(String userId);

    Map<String, Long> getUserCount();
}
