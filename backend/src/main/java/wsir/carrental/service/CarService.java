package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestParam;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.dto.CarDto;

import java.util.List;
import java.util.Map;

public interface CarService {
    IPage<CarDto> getPages(String carTypeId,
                           String carNameId,
                           String carSpecificName,
                           String carTag,
                           String licensePlate,
                           String rented,
                           CarStatus status,
                           Long current,
                           Long size);

    CarDto getOne(String carId);

    CarDto getOneByName(String specificName);

    Integer insertOne(Car car);

    Integer deleteBatch(List<String> ids);

    Integer chgCar(Car car);

    IPage<CarDto> getPages(String context,
                           Long current,
                           Long size);

    List<CarDto> getRandomFor3Cars(String carType);

    Map<String, Long> getCarCount();

    List<Map<String, Object>> getCarTypeChart();

    List<Map<String, Object>> getCarNameChart();
}
