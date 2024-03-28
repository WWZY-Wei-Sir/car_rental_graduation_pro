package wsir.carrental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.domain.CarType;
import wsir.carrental.mapper.CarMapper;
import wsir.carrental.mapper.CarTypeMapper;
import wsir.carrental.service.CarTypeService;

import java.util.List;

@Service
public class CarTypeServiceImpl implements CarTypeService {
    @Autowired
    private CarTypeMapper carTypeMapper;

    @Autowired
    private CarMapper carMapper;

    @Override
    @Transactional
    public List<CarType> getCarTypeList() {
        LambdaQueryWrapper<CarType> queryWrapper = new LambdaQueryWrapper<CarType>()
                .orderByDesc(CarType::getType);
        return carTypeMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public IPage<CarType> getPages(String type,
                                   Long current,
                                   Long size) {
        LambdaQueryWrapper<CarType> wrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isBlank(type)) {
            wrapper.eq(CarType::getType, type);
        }

        Page<CarType> page = new Page<>(current, size);
        return carTypeMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(CarType carType) {
        return carTypeMapper.insert(carType);
    }

    @Override
    @Transactional
    public Integer deleteBatch(List<String> ids) {
        ids.forEach(e -> {
            carMapper.delete(new LambdaQueryWrapper<Car>().eq(Car::getCarTypeId, e));
        });
        return carTypeMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public Integer chgCarType(CarType carType) {
        return carTypeMapper.updateById(carType);
    }
}
