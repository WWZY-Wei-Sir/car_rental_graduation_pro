package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.service.CarNameService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carName")
public class CarNameController {
    @Autowired
    private CarNameService carNameService;

    @GetMapping("getCarNameList")
    public Result<List<CarName>> getCarNameList()  {
        return Result.success(carNameService.getCarNameList());
    }

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String name,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<CarName> pages = carNameService.getPages(name, current, size);
        return Result.success(Map.of("page", pages));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody CarName carName) {
        if (carNameService.insertOne(carName) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加汽车品牌失败！");
    }

    @PostMapping("/delMore")
    public <T> Result<T> deleteBatch(@RequestBody List<String> ids) {
        if (carNameService.deleteBatch(ids) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除汽车品牌失败！");
    }

    @PostMapping("/chgCarName")
    public <T> Result<T> chgCarName(@RequestBody CarName carName) {
        if (carNameService.chgCarName(carName) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "更改汽车品牌失败！");
    }
}
