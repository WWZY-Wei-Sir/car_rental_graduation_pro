package wsir.carrental.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.entity.domain.CarUserComment;
import wsir.carrental.entity.domain.Report;
import wsir.carrental.entity.domain.User;
import wsir.carrental.entity.dto.ReportDto;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.mapper.CarUserCommentMapper;
import wsir.carrental.mapper.ReportMapper;
import wsir.carrental.mapper.UserMapper;
import wsir.carrental.service.ReportService;

import java.net.HttpURLConnection;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private CarUserCommentMapper carUserCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public IPage<ReportDto> getPages(String finished,
                                     Long current,
                                     Long size) {
        MPJLambdaWrapper<Report> wrapper = new MPJLambdaWrapper<Report>()
                .selectAll(Report.class)
                .selectAs(CarUserComment::getComment, ReportDto::getComment)
                .select("user1.user_name", ReportDto::getUserName)
                .select("user2.user_name", ReportDto::getPublisherName)
                .leftJoin(CarUserComment.class, CarUserComment::getId, Report::getCommentId)
                .leftJoin(User.class, "user1", User::getId, Report::getUserId)
                .leftJoin(User.class, "user2", User::getId, CarUserComment::getUserId)
                .orderByDesc(Report::getFinished);
        if (StrUtil.isNotBlank(finished)) {
            wrapper.eq(Report::getFinished, Boolean.parseBoolean(finished));
        }

        Page<ReportDto> page = new Page<>(current, size);
        return reportMapper.selectJoinPage(page, ReportDto.class, wrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(Report report) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<Report>()
                .eq(Report::getUserId, report.getUserId())
                .eq(Report::getCommentId, report.getCommentId());
        if (ObjectUtil.isNotNull(reportMapper.selectOne(wrapper))) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "该评论您已举报过，请等待管理员的处理！");
        }
        return reportMapper.insert(report);
    }

    @Override
    @Transactional
    public void lock(Report report) {
        report.setUpdateBy(null);
        report.setUpdateTime(null);
        report.setFinished(Boolean.TRUE);
        reportMapper.updateById(report);

        CarUserComment carUserComment = carUserCommentMapper.selectOne(new LambdaQueryWrapper<CarUserComment>().eq(CarUserComment::getId, report.getCommentId()));
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, carUserComment.getUserId()));
        user.setStatus(UserStatus.Block);
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public Integer ignore(Report report) {
        report.setUpdateBy(null);
        report.setUpdateTime(null);
        report.setFinished(Boolean.TRUE);
        return reportMapper.updateById(report);
    }
}
