package wsir.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.ChatRecord;
import wsir.carrental.entity.dto.ChatRecordDto;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.service.ChatRecordService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatRecord")
public class ChatRecordController {
    @Autowired
    private ChatRecordService chatRecordService;

    @RequestMapping("/sendMsg")
    public <T> Result<T> sendMsg(@RequestBody ChatRecord chatRecord) {
        if (chatRecordService.sendMsg(chatRecord) > 0) {
            return Result.success();
        }
        throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "发送信息失败");
    }

    @RequestMapping("/getMsg")
    public Result<List<ChatRecordDto>> getMsg(@RequestParam String userId) {
        return Result.success(chatRecordService.getMsg(userId));
    }
}
