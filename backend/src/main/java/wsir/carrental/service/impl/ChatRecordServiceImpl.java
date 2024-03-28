package wsir.carrental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.ChatRecord;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.ChatRecordDto;
import wsir.carrental.entity.vo.UserVo;
import wsir.carrental.mapper.ChatRecordMapper;
import wsir.carrental.service.ChatRecordService;

import java.util.Collections;
import java.util.List;

@Service
public class ChatRecordServiceImpl implements ChatRecordService {
    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Override
    @Transactional
    public Integer sendMsg(ChatRecord chatRecord) {
        return chatRecordMapper.insert(chatRecord);
    }

    @Override
    @Transactional
    public List<ChatRecordDto> getMsg(String userId) {
        MPJLambdaWrapper<ChatRecord> wrapper = new MPJLambdaWrapper<ChatRecord>()
                .selectAll(ChatRecord.class)
                .selectAs(User::getUserName, ChatRecordDto::getRecipientUserName)
                .select("user2.user_name", ChatRecordDto::getSpeakerUserName)
                .leftJoin(User.class, "user1", User::getId, ChatRecord::getRecipientUserId)
                .leftJoin(User.class, "user2", User::getId, ChatRecord::getSpeakerUserId)
                .disableSubLogicDel()
                .orderByDesc(ChatRecord::getCreateTime);
        if (StrUtil.isNotBlank(userId)) {
            wrapper.eq(ChatRecord::getSpeakerUserId, userId).or().eq(ChatRecord::getRecipientUserId, userId);
        }

        Page<ChatRecordDto> page = new Page<>(1, 20);
        List<ChatRecordDto> records = chatRecordMapper.selectJoinPage(page, ChatRecordDto.class, wrapper).getRecords();
        Collections.reverse(records);
        return records;
    }
}
