package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("car_type")
public class CarType extends LogicDelAndVerObject {
    String type;
}
