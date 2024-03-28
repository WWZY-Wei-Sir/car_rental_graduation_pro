package wsir.carrental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.entity.domain.Report;
import wsir.carrental.entity.dto.ReportDto;
import wsir.carrental.service.ReportService;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/getPages")
    public Result<Map<String, Object>> getPages(@RequestParam String finished,
                                                @RequestParam Long current,
                                                @RequestParam Long size) {
        IPage<ReportDto> pages = reportService.getPages(finished, current, size);
        return Result.success(Map.of("page", pages));
    }

    @PostMapping("/insertOne")
    public <T> Result<T> insertOne(@RequestBody Report report) {
        if (reportService.insertOne(report) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "举报失败！");
    }

    @PostMapping("/lock")
    public <T> Result<T> lock(@RequestBody Report report) {
        reportService.lock(report);
        return Result.success();
    }

    @PostMapping("/ignore")
    public <T> Result<T> ignore(@RequestBody Report report) {
        if (reportService.ignore(report) > 0) {
            return Result.success();
        }
        return Result.error(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "忽略举报操作失败！");
    }
}
