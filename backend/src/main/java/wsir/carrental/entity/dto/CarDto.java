package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.domain.CarConfig;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarDto extends Car {
    private String carType;
    private String carName;
    private String url;
    private List<CarConfig> carConfigs;
    private Long chartNumber;
}
