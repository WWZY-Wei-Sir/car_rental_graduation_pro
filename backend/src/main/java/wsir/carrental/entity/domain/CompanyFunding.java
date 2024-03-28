package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("company_funding")
public class CompanyFunding extends LogicDelAndVerObject {
    private Date date;
    private BigDecimal changeFunds;
}
