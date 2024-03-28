package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.CarType;

import java.util.List;

public interface CarNameService {
    List<CarName> getCarNameList();

    IPage<CarName> getPages(String name,
                            Long current,
                            Long size);

    Integer insertOne(CarName carName);

    Integer deleteBatch(List<String> ids);

    Integer chgCarName(CarName carName);
}
