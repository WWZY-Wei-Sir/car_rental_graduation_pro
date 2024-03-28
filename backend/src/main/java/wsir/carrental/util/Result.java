package wsir.carrental.util;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private static final String dateTimeFormat = "YYYYMMddHHmmss";
    private String dateTime;
    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(DateUtil.format(LocalDateTime.now(), dateTimeFormat), Boolean.TRUE, HttpURLConnection.HTTP_OK, "", data);
    }

    public static <T> Result<T> error() {
        return error(HttpURLConnection.HTTP_INTERNAL_ERROR, "服务器错误");
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(DateUtil.format(LocalDateTime.now(), dateTimeFormat), Boolean.FALSE, code, msg, null);
    }
}
