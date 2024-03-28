package wsir.carrental.service;

import wsir.carrental.entity.domain.CarConfig;

import java.util.List;

public interface CarConfigService {
    List<CarConfig> getCarConfigList(String carId);

    Integer insertOne(CarConfig carConfig);

    Integer deleteOne(CarConfig carConfig);

    Integer deleteByCarId(String carId);
}
