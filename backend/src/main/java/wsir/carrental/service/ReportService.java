package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.Report;
import wsir.carrental.entity.dto.ReportDto;

import java.util.List;

public interface ReportService {
    IPage<ReportDto> getPages(String finished,
                              Long current,
                              Long size);

    Integer insertOne(Report report);

    void lock(Report report);

    Integer ignore(Report report);
}
