package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.dict.OrderStatus;
import wsir.carrental.entity.dto.OrderDto;
import wsir.carrental.entity.vo.OrderVo;

import java.util.Map;

public interface OrderService {
    void generateOrder(OrderVo orderVo);

    void depositToAudit(OrderVo orderVo);

    void auditToTakeCar(OrderVo orderVo);

    Integer takeCarToCheckFile(OrderVo orderVo);

    Integer checkFileToReturnCar(OrderVo orderVo);

    void returnCarToWorkerCal(OrderVo orderVo);

    Integer workerCalToRemaining(OrderVo orderVo);

    void remainingToFinish(OrderVo orderVo);

    OrderDto getOne(String userId);

    void cancel(OrderVo orderVo);

    IPage<OrderDto> getPages(String userId, OrderStatus status, Long current, Long size);

    OrderDto getOneDriverMsg(String orderId);

    Integer chgOrder(OrderVo orderVo);

    Map<String, Long> getOrderCount();
}
