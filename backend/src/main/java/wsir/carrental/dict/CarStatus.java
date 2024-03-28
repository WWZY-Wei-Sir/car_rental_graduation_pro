package wsir.carrental.dict;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CarStatus {
    Normal(0, "正常"),
    Maintenance(1, "维修中"),
    Annual_Inspection(2, "年检中"),
    Wrong(-1, "错误");

    @EnumValue
    private final Integer code;
    private final String msg;

    CarStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
