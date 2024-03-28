package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.dict.OrderStatus;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("car_order")
public class Order extends LogicDelAndVerObject {
    private String customerUserId;
    private String carId;
    private String driverLicense;
    private String frontSideImage;
    private String reverseSideImage;
    private LocalDateTime takeDate;
    private LocalDateTime returnDate;
    private LocalDateTime returnDateTrue;
    private String takePlace;
    private String returnPlace;
    private Boolean takePlaceChange;
    private Boolean returnPlaceChange;
    private Integer delayTimes;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private Boolean isNew;
}
