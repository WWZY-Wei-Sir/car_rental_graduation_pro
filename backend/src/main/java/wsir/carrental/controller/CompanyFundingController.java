package wsir.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wsir.carrental.entity.domain.CompanyFunding;
import wsir.carrental.service.CompanyFundingService;
import wsir.carrental.util.Result;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companyFunding")
public class CompanyFundingController {
    @Autowired
    private CompanyFundingService companyFundingService;

    @GetMapping("/getDailyFundingChart")
    public Result<Map<String, List<String>>> getDailyFundingChart() {
        return Result.success(companyFundingService.getDailyFundingChart());
    }
}
