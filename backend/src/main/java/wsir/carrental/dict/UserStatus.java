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
//@JSONType(serializeEnumAsJavaBean = true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatus {
    Normal(1, "正常"),
    Block(0, "封禁"),
    Wrong(-1, "错误");

    @EnumValue
    private final Integer code;
//    @JsonValue
    private final String msg;

    UserStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 自定义反序列函数
    // JsonCreator.Mode.DELEGATING： 接收单个值，将接收的值整个传入自定义函数
//    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserStatus get(Integer code) {
        if (code == null) return Wrong;
        return Arrays.stream(UserStatus.values()).filter(i -> i.getCode() == code.intValue()).findAny().orElse(Wrong);
    }

    public static UserStatus getByMsg(String msg) {
        if (StrUtil.isBlank(msg)) return Wrong;
        return Arrays.stream(UserStatus.values()).filter(i -> i.getMsg().equals(msg)).findAny().orElse(Wrong);
    }
}
