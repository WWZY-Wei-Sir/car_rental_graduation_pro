package wsir.carrental.entity.vo;

import lombok.*;
import wsir.carrental.entity.domain.Order;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderVo extends Order {
    private String workerId;
    private String mailbox;
    private String password;
    private BigDecimal chargeAmount;
    private BigDecimal dailyRent;
}
