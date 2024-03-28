package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.RotateImage;
import wsir.carrental.entity.dto.RotateImageDto;
import wsir.carrental.service.RotateImageService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rotateImage")
public class RotateImageController {
    @Autowired
    private RotateImageService rotateImageService;

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<RotateImageDto> pages = rotateImageService.getPages(current, size);
        return Result.success(Map.of("page", pages));
    }

    @GetMapping("/getList")
    public Result<List<RotateImageDto>> getPages() {
        return Result.success(rotateImageService.getList());
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody RotateImage rotateImage) {
        if (rotateImageService.insertOne(rotateImage) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "添加轮播图失败！");
    }

    @PostMapping("/delMore")
    public <T> Result<T> deleteBatch(@RequestBody List<String> ids) {
        if (rotateImageService.deleteBatch(ids) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "删除轮播图失败！");
    }

    @PostMapping("/chgOne")
    public <T> Result<T> chgCarName(@RequestBody RotateImage rotateImage) {
        if (rotateImageService.chgOne(rotateImage) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "更改轮播图失败！");
    }
}
