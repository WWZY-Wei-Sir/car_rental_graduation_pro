package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.CarType;

import java.util.List;

public interface CarTypeService {
    List<CarType> getCarTypeList();

    IPage<CarType> getPages(String type,
                           Long current,
                           Long size);

    Integer insertOne(CarType carType);

    Integer deleteBatch(List<String> ids);

    Integer chgCarType(CarType carType);
}
