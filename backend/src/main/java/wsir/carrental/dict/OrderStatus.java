package wsir.carrental.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    WAIT_DEPOSIT(1, "待下定金"),
    WAIT_AUDIT(2, "待审核"),
    WAIT_TAKE_CAR(3, "待取车"),
    WAIT_CHECK_FILE(4, "待检查文件"),
    WAIT_RETURN_CAR(5, "待还车"),
    WAIT_WORKER_CAL(6, "待工作人员检查"),
    WAIT_REMAINING(7, "待付剩余金额"),
    CANCEL(10, "取消"),
    FINISH(11, "结束"),
    Wrong(-1, "错误");

    @EnumValue
    private final Integer code;
    private final String msg;

    OrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
