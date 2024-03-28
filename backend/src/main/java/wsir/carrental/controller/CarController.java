package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.dto.CarDto;
import wsir.carrental.service.CarService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String carTypeId,
                                                @RequestParam String carNameId,
                                                @RequestParam String carSpecificName,
                                                @RequestParam String carTag,
                                                @RequestParam String licensePlate,
                                                @RequestParam String rented,
                                                @RequestParam CarStatus status,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<CarDto> pages = carService.getPages(carTypeId, carNameId, carSpecificName, carTag, licensePlate, rented, status, current, size);
        return Result.success(Map.of("page", pages));
    }

    @GetMapping("/getOne")
    public Result<CarDto> getOne(@RequestParam String carId) {
        return Result.success(carService.getOne(carId));
    }

    @GetMapping("/getOneByName")
    public Result<CarDto> getOneByName(@RequestParam String specificName) {
        return Result.success(carService.getOneByName(specificName));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody Car car) {
        if (carService.insertOne(car) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加汽车信息失败！");
    }

    @PostMapping("/delMore")
    public <T> Result<T> deleteBatch(@RequestBody List<String> ids) {
        if (carService.deleteBatch(ids) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除汽车信息失败！");
    }

    @PostMapping("/chgCar")
    public <T> Result<T> chgCar(@RequestBody Car car) {
        if (carService.chgCar(car) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "更改汽车信息失败！");
    }

    @GetMapping("/getSearchPages")
    public Result<Map<String, Object>> getSearchPages(@RequestParam String context,
                                                      @RequestParam Long current,
                                                      @RequestParam Long size) {
        IPage<CarDto> pages = carService.getPages(context, current, size);
        return Result.success(Map.of("page", pages));
    }

    @GetMapping("/getRandomFor3Cars")
    public Result<List<CarDto>> getRandomFor3Cars(@RequestParam String carType) {
        return Result.success(carService.getRandomFor3Cars(carType));
    }

    @GetMapping("/getCarCount")
    public Result<Map<String, Long>> getCarCount() {
        return Result.success(carService.getCarCount());
    }

    @GetMapping("/getCarTypeChart")
    public Result<List<Map<String, Object>>> getCarTypeChart() {
        return Result.success(carService.getCarTypeChart());
    }

    @GetMapping("/getCarNameChart")
    public Result<List<Map<String, Object>>> getCarNameChart() {
        return Result.success(carService.getCarNameChart());
    }
}
