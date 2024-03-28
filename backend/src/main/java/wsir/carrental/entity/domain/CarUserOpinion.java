package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("car_user_opinion")
public class CarUserOpinion extends LogicDelAndVerObject {
    private String userId;
    private String carId;
    private Boolean collected;
    private Boolean liked;
}
