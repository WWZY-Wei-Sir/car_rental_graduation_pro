package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestParam;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.ChatRecord;
import wsir.carrental.entity.dto.ChatRecordDto;
import wsir.carrental.util.Result;

import java.util.List;

public interface ChatRecordService {
    Integer sendMsg(ChatRecord chatRecord);

    List<ChatRecordDto> getMsg(String userId);
}
