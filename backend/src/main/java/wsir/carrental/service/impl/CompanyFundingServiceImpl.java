package wsir.carrental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.CompanyFunding;
import wsir.carrental.mapper.CompanyFundingMapper;
import wsir.carrental.service.CompanyFundingService;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CompanyFundingServiceImpl implements CompanyFundingService {
    @Autowired
    private CompanyFundingMapper companyFundingMapper;

    @Override
    @Transactional
    public Map<String, List<String>> getDailyFundingChart() {
        List<String> dateList = new ArrayList<>();
        List<String> fundList = new ArrayList<>();
        List<CompanyFunding> list = companyFundingMapper.selectList(new LambdaQueryWrapper<CompanyFunding>().orderByDesc(CompanyFunding::getDate));
        if (list.size() >= 7) {
            list = list.subList(0, 7);
        }
        Collections.reverse(list);
        int i = 0;
        for (CompanyFunding companyFunding : list) {
            if (i >= 7) {
                break;
            }
            dateList.add(companyFunding.getDate().toString());
            fundList.add(companyFunding.getChangeFunds().toString());
            ++i;
        }
        return Map.of("xAxis", dateList, "funding", fundList);
    }
}
