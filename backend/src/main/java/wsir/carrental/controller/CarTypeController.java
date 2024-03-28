package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.CarType;
import wsir.carrental.service.CarTypeService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carType")
public class CarTypeController {
    @Autowired
    private CarTypeService carTypeService;

    @GetMapping("getCarTypeList")
    public Result<List<CarType>> getCarTypeList()  {
        return Result.success(carTypeService.getCarTypeList());
    }

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String type,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<CarType> pages = carTypeService.getPages(type, current, size);
        return Result.success(Map.of("page", pages));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody CarType carType) {
        if (carTypeService.insertOne(carType) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加汽车类别失败！");
    }

    @PostMapping("/delMore")
    public <T> Result<T> deleteBatch(@RequestBody List<String> ids) {
        if (carTypeService.deleteBatch(ids) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除汽车类别失败！");
    }

    @PostMapping("/chgCarType")
    public <T> Result<T> chgCarType(@RequestBody CarType carType) {
        if (carTypeService.chgCarType(carType) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "更改汽车类别失败！");
    }
}
