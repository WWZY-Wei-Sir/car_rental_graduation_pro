package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.CarUserComment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarUserCommentDto extends CarUserComment {
    private String userName;
}
