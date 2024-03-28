package wsir.carrental.dict;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserType {
    Admin(1, "管理员"),
    Worker(2, "员工"),
    Customer(3, "顾客"),
    Wrong(-1, "错误");

    @EnumValue
    private final Integer code;
    private final String msg;

    UserType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserType get(Integer code) {
        if (code == null) return Wrong;
        return Arrays.stream(UserType.values()).filter(i -> i.getCode() == code.intValue()).findAny().orElse(Wrong);
    }

    public static UserType getByMsg(String msg) {
        if (StrUtil.isBlank(msg)) return Wrong;
        return Arrays.stream(UserType.values()).filter(i -> i.getMsg().equals(msg)).findAny().orElse(Wrong);
    }
}
