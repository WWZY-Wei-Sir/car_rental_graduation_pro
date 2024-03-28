package wsir.carrental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.CarType;
import wsir.carrental.mapper.CarMapper;
import wsir.carrental.mapper.CarNameMapper;
import wsir.carrental.service.CarNameService;

import java.util.List;

@Service
public class CarNameServiceImpl implements CarNameService {
    @Autowired
    private CarNameMapper carNameMapper;

    @Autowired
    private CarMapper carMapper;

    @Override
    @Transactional
    public List<CarName> getCarNameList() {
        LambdaQueryWrapper<CarName> queryWrapper = new LambdaQueryWrapper<CarName>()
                .orderByDesc(CarName::getName);
        return carNameMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public IPage<CarName> getPages(String name,
                                   Long current,
                                   Long size) {
        LambdaQueryWrapper<CarName> wrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isBlank(name)) {
            wrapper.eq(CarName::getName, name);
        }

        Page<CarName> page = new Page<>(current, size);
        return carNameMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(CarName carName) {
        return carNameMapper.insert(carName);
    }

    @Override
    @Transactional
    public Integer deleteBatch(List<String> ids) {
        ids.forEach(e -> {
            carMapper.delete(new LambdaQueryWrapper<Car>().eq(Car::getCarNameId, e));
        });
        return carNameMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public Integer chgCarName(CarName carName) {
        return carNameMapper.updateById(carName);
    }
}
