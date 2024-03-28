package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends User {
    private String token;
    private String url;
    private String frontSideUrl;
    private String reverseSideUrl;
}
