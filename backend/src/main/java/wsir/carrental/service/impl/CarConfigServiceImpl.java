package wsir.carrental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.CarConfig;
import wsir.carrental.entity.domain.basic.BasicObject;
import wsir.carrental.mapper.CarConfigMapper;
import wsir.carrental.service.CarConfigService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarConfigServiceImpl implements CarConfigService {
    @Autowired
    private CarConfigMapper carConfigMapper;

    @Override
    @Transactional
    public List<CarConfig> getCarConfigList(String carId) {
        LambdaQueryWrapper<CarConfig> queryWrapper = new LambdaQueryWrapper<CarConfig>()
                .eq(CarConfig::getCarId, carId)
                .orderByDesc(CarConfig::getDetail);
        return carConfigMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(CarConfig carConfig) {
        carConfig.setCreateBy(null);
        return carConfigMapper.insert(carConfig);
    }

    @Override
    @Transactional
    public Integer deleteOne(CarConfig carConfig) {
        return carConfigMapper.deleteById(carConfig);
    }

    @Override
    @Transactional
    public Integer deleteByCarId(String carId) {
        List<CarConfig> carConfigs = carConfigMapper.selectList(new LambdaQueryWrapper<CarConfig>().eq(CarConfig::getCarId, carId));
        List<String> ids = carConfigs.stream().map(BasicObject::getId).collect(Collectors.toList());
        return carConfigMapper.deleteBatchIds(ids);
    }
}
