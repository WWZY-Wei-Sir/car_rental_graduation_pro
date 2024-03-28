package wsir.carrental.entity.vo;

import lombok.*;
import wsir.carrental.entity.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserVo extends User {
    private String passCode;
    private String token;
}
