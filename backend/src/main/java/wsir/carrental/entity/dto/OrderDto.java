package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.CarConfig;
import wsir.carrental.entity.domain.Order;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderDto extends Order {
    private String url;
    private String frontSideUrl;
    private String reverseSideUrl;
    private String carSpecificName;
    private String licensePlate;
    private BigDecimal dailyRent;
    private String userName;
}
