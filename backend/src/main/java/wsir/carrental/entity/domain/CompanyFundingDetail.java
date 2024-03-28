package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("company_funding_detail")
public class CompanyFundingDetail extends LogicDelAndVerObject {
    private BigDecimal changeFunds;
    private Boolean direction;
    private String reason;
    private String note;
}
