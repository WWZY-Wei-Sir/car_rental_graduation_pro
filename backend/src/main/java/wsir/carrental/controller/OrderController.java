package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.dict.OrderStatus;
import wsir.carrental.entity.dto.OrderDto;
import wsir.carrental.entity.vo.OrderVo;
import wsir.carrental.service.OrderService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/rent")
    public <T> Result<T> generateOrder(@RequestBody OrderVo orderVo) {
        orderService.generateOrder(orderVo);
        return Result.success();
    }

    @PostMapping("/chargeDeposit")
    public <T> Result<T> depositToAudit(@RequestBody OrderVo orderVo) {
        orderService.depositToAudit(orderVo);
        return Result.success();
    }

    @PostMapping("/audit")
    public <T> Result<T> auditToTakeCar(@RequestBody OrderVo orderVo) {
        orderService.auditToTakeCar(orderVo);
        return Result.success();
    }

    @PostMapping("/takeCar")
    public <T> Result<T> takeCarToCheckFile(@RequestBody OrderVo orderVo) {
        if (orderService.takeCarToCheckFile(orderVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户取车失败");
    }

    @PostMapping("/checkFile")
    public <T> Result<T> checkFileToReturnCar(@RequestBody OrderVo orderVo) {
        if (orderService.checkFileToReturnCar(orderVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_CONFLICT, "工作人员检查文件失败");
    }

    @PostMapping("/returnCar")
    public <T> Result<T> returnCarToWorkerCal(@RequestBody OrderVo orderVo) {
        orderService.returnCarToWorkerCal(orderVo);
        return Result.success();
    }

    @PostMapping("/calAmount")
    public <T> Result<T> workerCalToRemaining(@RequestBody OrderVo orderVo) {
        if (orderService.workerCalToRemaining(orderVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_CONFLICT, "工作人员设置总金额失败");
    }

    @PostMapping("/chargeRemaining")
    public <T> Result<T> remainingToFinish(@RequestBody OrderVo orderVo) {
        orderService.remainingToFinish(orderVo);
        return Result.success();
    }

    @GetMapping("/getOne")
    public Result<OrderDto> getOne(@RequestParam String userId) {
        return Result.success(orderService.getOne(userId));
    }

    @PostMapping("/cancel")
    public <T> Result<T> cancel(@RequestBody OrderVo orderVo) {
        orderService.cancel(orderVo);
        return Result.success();
    }

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String userId,
                                                @RequestParam OrderStatus status,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<OrderDto> pages = orderService.getPages(userId, status, current, size);
        return Result.success(Map.of("page", pages));
    }

    @GetMapping("/getOneDriverMsg")
    public Result<OrderDto> getOneDriverMsg(@RequestParam String orderId) {
        return Result.success(orderService.getOneDriverMsg(orderId));
    }

    @PostMapping("/changePlace")
    public <T> Result<T> changePlace(@RequestBody OrderVo orderVo) {
        if (orderService.chgOrder(orderVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户修改地点失败");
    }

    @PostMapping("/changeReturnDate")
    public <T> Result<T> changeReturnDate(@RequestBody OrderVo orderVo) {
        if (orderService.chgOrder(orderVo) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_CONFLICT, "用户延长租赁时间失败");
    }

    @GetMapping("/getOrderCount")
    public Result<Map<String, Long>> getuserCount() {
        return Result.success(orderService.getOrderCount());
    }
}
