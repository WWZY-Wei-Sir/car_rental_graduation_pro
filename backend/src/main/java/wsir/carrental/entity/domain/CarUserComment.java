package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("car_user_comment")
public class CarUserComment extends LogicDelAndVerObject {
    private String userId;
    private String carId;
    private String comment;
    private Float rate;
}
