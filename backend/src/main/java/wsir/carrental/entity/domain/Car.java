package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.dict.CarStatus;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("car")
public class Car extends LogicDelAndVerObject {
    private String carTypeId;
    private String carNameId;
    private String imageId;
    private String carSpecificName;
    private String carTag;
    private String licensePlate;
    private BigDecimal dailyRent;
    private Boolean rented;
    private CarStatus status;
}
