package wsir.carrental.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.dict.OrderStatus;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.entity.domain.*;
import wsir.carrental.entity.dto.OrderDto;
import wsir.carrental.entity.vo.OrderVo;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.mapper.*;
import wsir.carrental.service.MailService;
import wsir.carrental.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CompanyFundingDetailMapper companyFundingDetailMapper;

    @Autowired
    private CompanyFundingMapper companyFundingMapper;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public void generateOrder(OrderVo orderVo) {
        orderVo.setTakePlaceChange(Boolean.FALSE);
        orderVo.setReturnPlaceChange(Boolean.FALSE);
        orderVo.setDelayTimes(0);
        orderVo.setFinalAmount(BigDecimal.ZERO);
        orderVo.setStatus(OrderStatus.WAIT_DEPOSIT);
        orderVo.setIsNew(Boolean.TRUE);
        orderVo.setCreateBy(null);
        orderVo.setCreateTime(null);
        orderVo.setUpdateBy(null);
        orderVo.setUpdateTime(null);
        orderVo.setDeleted(Boolean.FALSE);
        orderVo.setVersion(1);
        orderMapper.insert(orderVo);

        User user = userMapper.selectById(orderVo.getCustomerUserId());
        user.setRentCar(Boolean.TRUE);
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void depositToAudit(OrderVo orderVo) {
        User user = userMapper.selectById(orderVo.getCustomerUserId());
        if (checkPwdFail(user, orderVo.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "支付密码错误");
        }
        BigDecimal result = user.getBalance().subtract(orderVo.getChargeAmount());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "用户余额不足");
        }
        user.setBalance(result);
        userMapper.updateById(user);

        Car car = carMapper.selectById(orderVo.getCarId());
        if (car.getRented()) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "该车辆已被租借");
        }
        car.setRented(Boolean.TRUE);
        carMapper.updateById(car);

        orderVo.setStatus(OrderStatus.WAIT_AUDIT);
        orderMapper.updateById(orderVo);

        CompanyFundingDetail companyFundingDetail = new CompanyFundingDetail();
        companyFundingDetail.setChangeFunds(orderVo.getChargeAmount());
        companyFundingDetail.setDirection(Boolean.TRUE);
        companyFundingDetail.setReason("用户支付定金");
        companyFundingDetail.setCreateBy(null);
        companyFundingDetailMapper.insert(companyFundingDetail);

        LambdaQueryWrapper<CompanyFunding> wrapper = new LambdaQueryWrapper<CompanyFunding>()
                .eq(CompanyFunding::getDate, DateUtil.date().toSqlDate());
        CompanyFunding companyFunding;
        if ((companyFunding = companyFundingMapper.selectOne(wrapper)) != null) {
            companyFunding.setChangeFunds(companyFunding.getChangeFunds().add(orderVo.getChargeAmount()));
            companyFundingMapper.updateById(companyFunding);
        } else {
            companyFunding = new CompanyFunding();
            companyFunding.setDate(DateUtil.date().toJdkDate());
            companyFunding.setChangeFunds(orderVo.getChargeAmount());
            companyFundingMapper.insert(companyFunding);
        }
    }

    @Override
    @Transactional
    public void auditToTakeCar(OrderVo orderVo) {
        User user = userMapper.selectById(orderVo.getWorkerId());
        if (checkPwdFail(user, orderVo.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "支付密码错误");
        }
        orderVo.setStatus(OrderStatus.WAIT_TAKE_CAR);
        orderMapper.updateById(orderVo);
        mailService.sendCompanyLicense(userMapper.selectById(orderVo.getCustomerUserId()).getEmail());
    }

    @Override
    @Transactional
    public Integer takeCarToCheckFile(OrderVo orderVo) {
        orderVo.setStatus(OrderStatus.WAIT_CHECK_FILE);
        DateTime nowDate = DateUtil.date();
        if (DateUtil.date(orderVo.getTakeDate()).getTime() > nowDate.getTime()) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "取车时间不得早于当天");
        }
        if (DateUtil.date(orderVo.getTakeDate()).getTime() + 12 * 60 * 60 * 1000 > nowDate.getTime()) {
            orderVo.setTakeDate(nowDate.toLocalDateTime());
        }
        return orderMapper.updateById(orderVo);
    }

    @Override
    @Transactional
    public Integer checkFileToReturnCar(OrderVo orderVo) {
        orderVo.setStatus(OrderStatus.WAIT_RETURN_CAR);
        return orderMapper.updateById(orderVo);
    }

    @Override
    @Transactional
    public void returnCarToWorkerCal(OrderVo orderVo) {
        orderVo.setStatus(OrderStatus.WAIT_WORKER_CAL);
        DateTime nowDate = DateUtil.date();
        if (DateUtil.date(orderVo.getReturnDateTrue()).getTime() > nowDate.getTime()) {
            orderVo.setReturnDateTrue(orderVo.getReturnDateTrue());
        } else {
            orderVo.setReturnDateTrue(nowDate.toLocalDateTime());
        }
        orderMapper.updateById(orderVo);

        Car car = carMapper.selectById(orderVo.getCarId());
        car.setRented(Boolean.FALSE);
        carMapper.updateById(car);
    }

    @Override
    @Transactional
    public Integer workerCalToRemaining(OrderVo orderVo) {
        User user = userMapper.selectById(orderVo.getWorkerId());
        if (checkPwdFail(user, orderVo.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "支付密码错误");
        }
        Long startTime = DateUtil.date(orderVo.getTakeDate()).getTime();
        Long endTime = DateUtil.date(orderVo.getReturnDate()).getTime();
        Long endTimeTrue = DateUtil.date(orderVo.getReturnDateTrue()).getTime();
        BigDecimal hourRent = orderVo.getDailyRent().divide(new BigDecimal(24), 2, RoundingMode.HALF_UP);
        BigDecimal basicAmount;
        //  是否超时还车的计算
        if (endTimeTrue - endTime > 24 * 60 * 60 * 1000) {
            BigDecimal first = hourRent.multiply(new BigDecimal(((endTime - startTime) / 60 / 60 / 1000) + 1));
            BigDecimal second = hourRent.multiply(new BigDecimal(((endTimeTrue - endTime) / 60 / 60 / 1000) + 1));
            basicAmount = first.add(second.multiply(new BigDecimal(3)));
        } else {
            basicAmount = hourRent.multiply(new BigDecimal(((endTimeTrue - startTime) / 60 / 60 / 1000) + 1));
        }
        //  租赁时长优惠的计算
        if (endTime - startTime >= 7 * 24 * 60 * 60 * 1000 && endTime - startTime < (long) 30 * 24 * 60 * 60 * 1000) {
            basicAmount = basicAmount.multiply(new BigDecimal(9));
        } else if (endTime - startTime >= (long) 30 * 24 * 60 * 60 * 1000 && endTime - startTime < (long) 180 * 24 * 60 * 60 * 1000) {
            basicAmount = basicAmount.multiply(new BigDecimal(8));
        } else {
            basicAmount = basicAmount.multiply(new BigDecimal(6));
        }
        basicAmount = basicAmount.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
        //  申请延长租赁时长的加收计算
        if (orderVo.getDelayTimes() > 0) {
            BigDecimal plus = new BigDecimal(orderVo.getDelayTimes()).multiply(basicAmount.divide(new BigDecimal(5), 2, RoundingMode.HALF_UP));
            basicAmount = basicAmount.add(plus);
        }
        orderVo.setStatus(OrderStatus.WAIT_REMAINING);
        orderVo.setFinalAmount(orderVo.getChargeAmount().add(basicAmount));
        return orderMapper.updateById(orderVo);
    }

    @Override
    @Transactional
    public void remainingToFinish(OrderVo orderVo) {
        User user = userMapper.selectById(orderVo.getCustomerUserId());
        if (checkPwdFail(user, orderVo.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "支付密码错误");
        }
        BigDecimal result = user.getBalance().subtract(orderVo.getFinalAmount());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "用户余额不足");
        }
        user.setBalance(result);
        user.setRentCar(Boolean.FALSE);
        userMapper.updateById(user);

        orderVo.setStatus(OrderStatus.FINISH);
        orderVo.setIsNew(Boolean.FALSE);
        orderMapper.updateById(orderVo);

        CompanyFundingDetail companyFundingDetail = new CompanyFundingDetail();
        companyFundingDetail.setChangeFunds(orderVo.getFinalAmount());
        companyFundingDetail.setDirection(Boolean.TRUE);
        companyFundingDetail.setReason("用户完成订单，支付最终金额");
        companyFundingDetail.setNote("最终金额已减去用户之前支付的订金");
        companyFundingDetail.setCreateBy(null);
        companyFundingDetailMapper.insert(companyFundingDetail);

        LambdaQueryWrapper<CompanyFunding> wrapper = new LambdaQueryWrapper<CompanyFunding>()
                .eq(CompanyFunding::getDate, DateUtil.date().toSqlDate());
        CompanyFunding companyFunding;
        if ((companyFunding = companyFundingMapper.selectOne(wrapper)) != null) {
            companyFunding.setChangeFunds(companyFunding.getChangeFunds().add(orderVo.getFinalAmount()));
            companyFundingMapper.updateById(companyFunding);
        } else {
            companyFunding = new CompanyFunding();
            companyFunding.setDate(DateUtil.date().toJdkDate());
            companyFunding.setChangeFunds(orderVo.getFinalAmount());
            companyFundingMapper.insert(companyFunding);
        }
    }

    @Override
    @Transactional
    public OrderDto getOne(String userId) {
        MPJLambdaWrapper<Order> wrapper = new MPJLambdaWrapper<Order>()
                .selectAll(Order.class)
                .selectAs(Image::getUrl, OrderDto::getUrl)
                .selectAs(Car::getCarSpecificName, OrderDto::getCarSpecificName)
                .selectAs(Car::getLicensePlate, OrderDto::getLicensePlate)
                .selectAs(Car::getDailyRent, OrderDto::getDailyRent)
                .selectAs(User::getUserName, OrderDto::getUserName)
                .leftJoin(Car.class, Car::getId, Order::getCarId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .leftJoin(User.class, User::getId, Order::getCustomerUserId)
                .eq(Order::getCustomerUserId, userId)
                .eq(Order::getIsNew, Boolean.TRUE);

        return orderMapper.selectJoinOne(OrderDto.class, wrapper);
    }

    @Override
    @Transactional
    public void cancel(OrderVo orderVo) {
        User user = userMapper.selectById(orderVo.getCustomerUserId());
        if (checkPwdFail(user, orderVo.getPassword())) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "支付密码错误");
        }
        user.setRentCar(Boolean.FALSE);

        Car car = carMapper.selectById(orderVo.getCarId());
        car.setRented(Boolean.FALSE);
        carMapper.updateById(car);

        orderVo.setStatus(OrderStatus.CANCEL);
        orderVo.setIsNew(Boolean.FALSE);

        if (orderVo.getStatus().equals(OrderStatus.WAIT_DEPOSIT)) {
            userMapper.updateById(user);
        } else {
            BigDecimal result;
            CompanyFundingDetail companyFundingDetail = new CompanyFundingDetail();
            if (orderVo.getStatus().equals(OrderStatus.WAIT_AUDIT)) {
                result = user.getBalance().add(orderVo.getChargeAmount());
            } else {
                orderVo.setChargeAmount(orderVo.getChargeAmount().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP));
                result = user.getBalance().add(orderVo.getChargeAmount());
                orderVo.setFinalAmount(result);
                companyFundingDetail.setNote("用户在工作人员审核后退款押金返还一半");
            }
            user.setBalance(result);
            userMapper.updateById(user);

            companyFundingDetail.setChangeFunds(orderVo.getChargeAmount());
            companyFundingDetail.setDirection(Boolean.FALSE);
            companyFundingDetail.setReason("用户取消订单");
            companyFundingDetail.setCreateBy(null);
            companyFundingDetailMapper.insert(companyFundingDetail);

            LambdaQueryWrapper<CompanyFunding> wrapper = new LambdaQueryWrapper<CompanyFunding>()
                    .eq(CompanyFunding::getDate, DateUtil.date().toSqlDate());
            CompanyFunding companyFunding;
            if ((companyFunding = companyFundingMapper.selectOne(wrapper)) != null) {
                companyFunding.setChangeFunds(companyFunding.getChangeFunds().subtract(orderVo.getChargeAmount()));
                companyFundingMapper.updateById(companyFunding);
            } else {
                companyFunding = new CompanyFunding();
                companyFunding.setDate(DateUtil.date().toJdkDate());
                companyFunding.setChangeFunds(BigDecimal.ZERO.subtract(orderVo.getChargeAmount()));
                companyFundingMapper.insert(companyFunding);
            }
        }
        orderMapper.updateById(orderVo);
    }

    @Override
    @Transactional
    public IPage<OrderDto> getPages(String userId,
                                    OrderStatus status,
                                    Long current,
                                    Long size) {
        MPJLambdaWrapper<Order> wrapper = new MPJLambdaWrapper<Order>()
                .selectAll(Order.class)
                .selectAs(Image::getUrl, OrderDto::getUrl)
                .selectAs(Car::getCarSpecificName, OrderDto::getCarSpecificName)
                .selectAs(Car::getLicensePlate, OrderDto::getLicensePlate)
                .selectAs(Car::getDailyRent, OrderDto::getDailyRent)
                .selectAs(User::getUserName, OrderDto::getUserName)
                .leftJoin(Car.class, Car::getId, Order::getCarId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .leftJoin(User.class, User::getId, Order::getCustomerUserId)
                .orderByDesc(Order::getIsNew)
                .orderByDesc(Order::getStatus);
        if (!OrderStatus.Wrong.equals(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        if (!StrUtil.isBlank(userId)) {
            wrapper.eq(Order::getCustomerUserId, userId);
            wrapper.eq(Order::getIsNew, Boolean.FALSE);
        }

        Page<OrderDto> page = new Page<>(current, size);
        return orderMapper.selectJoinPage(page, OrderDto.class, wrapper);
    }

    @Override
    @Transactional
    public OrderDto getOneDriverMsg(String orderId) {
        MPJLambdaWrapper<Order> wrapper = new MPJLambdaWrapper<Order>()
                .select("image1.url", OrderDto::getFrontSideUrl)
                .select("image2.url", OrderDto::getReverseSideUrl)
                .leftJoin(Image.class, "image1", Image::getId, Order::getFrontSideImage)
                .leftJoin(Image.class, "image2", Image::getId, Order::getReverseSideImage)
                .eq(Order::getId, orderId);
        return orderMapper.selectJoinOne(OrderDto.class, wrapper);
    }

    @Override
    @Transactional
    public Integer chgOrder(OrderVo orderVo) {
        return orderMapper.updateById(orderVo);
    }

    @Override
    @Transactional
    public Map<String, Long> getOrderCount() {
        Map<String, Long> result = new HashMap<>();
        result.put("total", orderMapper.selectCount(null));
        result.put("running", orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getIsNew, Boolean.TRUE)));
        result.put("cancel", orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus, OrderStatus.CANCEL)));
        result.put("finish", orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus, OrderStatus.FINISH)));
        return result;
    }

    private boolean checkPwdFail(User user, String password) {
        return !BCrypt.checkpw(password, user.getPassword());
    }
}
