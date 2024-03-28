package wsir.carrental.component;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wsir.carrental.entity.domain.User;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private UserPrincipal userPrincipal;

    @Override
    public void insertFill(MetaObject metaObject) {
        String currentUserName = getCurrentUser().getUserName();
        this.strictInsertFill(metaObject, "createBy", String.class, currentUserName);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateBy", String.class, currentUserName);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String currentUserName = getCurrentUser().getUserName();
        this.strictInsertFill(metaObject, "updateBy", String.class, currentUserName);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    public User getCurrentUser() {
        System.out.println(userPrincipal.getCurrentUser());
        if (ObjectUtil.isNotNull(userPrincipal.getCurrentUser())) {
            return userPrincipal.getCurrentUser();
        }

        User user = new User();
        user.setUserName("Personal");
        return user;
    }
}
