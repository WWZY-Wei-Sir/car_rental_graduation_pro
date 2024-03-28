package wsir.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.CarConfig;
import wsir.carrental.service.CarConfigService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;

@RestController
@RequestMapping("/carConfig")
public class CarConfigController {
    @Autowired
    private CarConfigService carConfigService;

    @GetMapping("getCarConfigList")
    public Result<List<CarConfig>> getCarConfigList(@RequestParam String carId)  {
        return Result.success(carConfigService.getCarConfigList(carId));
    }

    @PostMapping("/insertOne")
    public Result<CarConfig> insertOne(@RequestBody CarConfig carConfig) {
        if (carConfigService.insertOne(carConfig) > 0) {
            return Result.success(carConfig);
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加汽车配置失败！");
    }

    @PostMapping("/deleteOne")
    public <T> Result<T> deleteOne(@RequestBody CarConfig carConfig) {
        if (carConfigService.deleteOne(carConfig) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除汽车配置失败！");
    }
}
