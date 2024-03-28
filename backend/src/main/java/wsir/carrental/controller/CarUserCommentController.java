package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.CarUserComment;
import wsir.carrental.entity.dto.CarUserCommentDto;
import wsir.carrental.service.CarUserCommentService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.Map;

@RestController
@RequestMapping("/carUserComment")
public class CarUserCommentController {
    @Autowired
    private CarUserCommentService carUserCommentService;

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String carId,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<CarUserCommentDto> pages = carUserCommentService.getPages(carId, current, size);
        return Result.success(Map.of("page", pages));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody CarUserComment carUserComment) {
        if (carUserCommentService.insertOne(carUserComment) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "发布评论失败！");
    }
}
