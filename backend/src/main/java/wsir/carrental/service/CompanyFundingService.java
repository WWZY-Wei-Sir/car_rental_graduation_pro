package wsir.carrental.service;

import wsir.carrental.entity.domain.CompanyFunding;

import java.util.List;
import java.util.Map;

public interface CompanyFundingService {
    Map<String, List<String>> getDailyFundingChart();
}
