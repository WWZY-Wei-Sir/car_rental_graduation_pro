package wsir.carrental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.CarType;
import wsir.carrental.entity.domain.Image;
import wsir.carrental.entity.dto.CarDto;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.mapper.CarMapper;
import wsir.carrental.service.CarConfigService;
import wsir.carrental.service.CarService;

import java.net.HttpURLConnection;
import java.util.*;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private CarConfigService carConfigService;

    @Override
    @Transactional
    public IPage<CarDto> getPages(String carTypeId,
                                  String carNameId,
                                  String carSpecificName,
                                  String carTag,
                                  String licensePlate,
                                  String rented,
                                  CarStatus status,
                                  Long current,
                                  Long size) {
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .selectAll(Car.class)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectAs(Image::getUrl, CarDto::getUrl)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .leftJoin(Image.class, Image::getId, Car::getImageId);
        if (!StrUtil.isBlank(carTypeId)) {
            wrapper.eq(Car::getCarTypeId, carTypeId);
        }
        if (!StrUtil.isBlank(carNameId)) {
            wrapper.eq(Car::getCarNameId, carNameId);
        }
        if (!StrUtil.isBlank(carSpecificName)) {
            wrapper.like(Car::getCarSpecificName, carSpecificName);
        }
        if (!StrUtil.isBlank(carTag)) {
            wrapper.like(Car::getCarTag, carTag);
        }
        if (!StrUtil.isBlank(licensePlate)) {
            wrapper.eq(Car::getLicensePlate, licensePlate);
        }
        if (!StrUtil.isBlank(rented)) {
            wrapper.eq(Car::getRented, Boolean.parseBoolean(rented));
        }
        if (!CarStatus.Wrong.equals(status)) {
            wrapper.eq(Car::getStatus, status);
        }
        wrapper.orderByDesc("carType", "carName");

        Page<CarDto> page = new Page<>(current, size);
        return carMapper.selectJoinPage(page, CarDto.class, wrapper);
    }

    @Override
    @Transactional
    public CarDto getOne(String carId) {
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .selectAll(Car.class)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectAs(Image::getUrl, CarDto::getUrl)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .eq(Car::getId, carId);

        return carMapper.selectJoinOne(CarDto.class, wrapper);
    }

    @Override
    @Transactional
    public CarDto getOneByName(String specificName) {
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .selectAll(Car.class)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectAs(Image::getUrl, CarDto::getUrl)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .eq(Car::getCarSpecificName, specificName);

        return carMapper.selectJoinList(CarDto.class, wrapper).get(0);
    }

    @Override
    @Transactional
    public Integer insertOne(Car car) {
        LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<Car>()
                .eq(Car::getCarSpecificName, car.getCarSpecificName())
                .or()
                .eq(Car::getLicensePlate, car.getLicensePlate());
        if (carMapper.selectCount(wrapper) > 0) {
            throw new ServiceException(HttpURLConnection.HTTP_CONFLICT, "该汽车已存在");
        }
        return carMapper.insert(car);
    }

    @Override
    @Transactional
    public Integer deleteBatch(List<String> ids) {
        ids.forEach(e -> {
            carConfigService.deleteByCarId(e);
        });
        return carMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public Integer chgCar(Car car) {
        return carMapper.updateById(car);
    }

    @Override
    @Transactional
    public IPage<CarDto> getPages(String context,
                                  Long current,
                                  Long size) {
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .selectAll(Car.class)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectAs(Image::getUrl, CarDto::getUrl)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .like(CarType::getType, context)
                .or()
                .like(CarName::getName, context)
                .or()
                .like(Car::getCarSpecificName, context)
                .or()
                .like(Car::getCarTag, context)
                .orderByDesc("carType", "carName");

        Page<CarDto> page = new Page<>(current, size);
        return carMapper.selectJoinPage(page, CarDto.class, wrapper);
    }

    @Override
    @Transactional
    public List<CarDto> getRandomFor3Cars(String carType) {
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .selectAll(Car.class)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectAs(Image::getUrl, CarDto::getUrl)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .leftJoin(Image.class, Image::getId, Car::getImageId)
                .eq(CarType::getType, carType);

        List<CarDto> carDtoList = carMapper.selectJoinList(CarDto.class, wrapper);
        if (carDtoList.size() <= 3) {
            return carDtoList;
        }
        HashSet<Integer> set = new HashSet<>();
        randomSet(0, carDtoList.size() - 1, 3, set);
        List<CarDto> returnCarDtoList = new ArrayList<>();
        for (Integer integer : set) {
            returnCarDtoList.add(carDtoList.get(integer));
        }
        return returnCarDtoList;
    }

    @Override
    @Transactional
    public Map<String, Long> getCarCount() {
        Map<String, Long> result = new HashMap<>();
        result.put("total", carMapper.selectCount(null));
        result.put("rented", carMapper.selectCount(new LambdaQueryWrapper<Car>().eq(Car::getRented, Boolean.TRUE)));
        result.put("maintenance", carMapper.selectCount(new LambdaQueryWrapper<Car>().eq(Car::getStatus, CarStatus.Maintenance)));
        result.put("inspection", carMapper.selectCount(new LambdaQueryWrapper<Car>().eq(Car::getRented, CarStatus.Annual_Inspection)));
        return result;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getCarTypeChart() {
        List<Map<String, Object>> result = new ArrayList<>();
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .select(Car::getCarTypeId)
                .selectAs(CarType::getType, CarDto::getCarType)
                .selectCount(Car::getCarTypeId, CarDto::getChartNumber)
                .leftJoin(CarType.class, CarType::getId, Car::getCarTypeId)
                .groupBy(Car::getCarTypeId);
        for (CarDto carDto : carMapper.selectJoinList(CarDto.class, wrapper)) {
            result.add(Map.of("value", carDto.getChartNumber(), "name", carDto.getCarType()));
        }
        return result;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getCarNameChart() {
        List<Map<String, Object>> result = new ArrayList<>();
        MPJLambdaWrapper<Car> wrapper = new MPJLambdaWrapper<Car>()
                .select(Car::getCarNameId)
                .selectAs(CarName::getName, CarDto::getCarName)
                .selectCount(Car::getCarNameId, CarDto::getChartNumber)
                .leftJoin(CarName.class, CarName::getId, Car::getCarNameId)
                .groupBy(Car::getCarNameId);
        for (CarDto carDto : carMapper.selectJoinList(CarDto.class, wrapper)) {
            result.add(Map.of("value", carDto.getChartNumber(), "name", carDto.getCarName()));
        }
        return result;
    }

    private void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);
        }
        int setSize = set.size();
        // 递归生成指定大小的hashset
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }
}
