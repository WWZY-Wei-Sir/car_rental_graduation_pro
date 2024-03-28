package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("rotate_image")
public class RotateImage extends LogicDelAndVerObject {
    private String imageId;
    private String link;
    private String context;
}
