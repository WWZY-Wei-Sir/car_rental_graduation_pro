package wsir.carrental.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.entity.domain.CarUserComment;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.CarUserCommentDto;
import wsir.carrental.mapper.CarUserCommentMapper;
import wsir.carrental.service.CarUserCommentService;

@Service
public class CarUserCommentServiceImpl implements CarUserCommentService {
    @Autowired
    private CarUserCommentMapper carUserCommentMapper;

    @Override
    @Transactional
    public IPage<CarUserCommentDto> getPages(String carId, Long current, Long size) {
        MPJLambdaWrapper<CarUserComment> wrapper = new MPJLambdaWrapper<CarUserComment>()
                .selectAll(CarUserComment.class)
                .selectAs(User::getUserName, CarUserCommentDto::getUserName)
                .leftJoin(User.class, User::getId, CarUserComment::getUserId)
                .eq(CarUserComment::getCarId, carId)
                .eq(User::getStatus, UserStatus.Normal)
                .orderByDesc(CarUserComment::getCreateTime);

        Page<CarUserCommentDto> pages = new Page<>(current, size);
        return carUserCommentMapper.selectJoinPage(pages, CarUserCommentDto.class, wrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(CarUserComment carUserComment) {
        return carUserCommentMapper.insert(carUserComment);
    }
}
