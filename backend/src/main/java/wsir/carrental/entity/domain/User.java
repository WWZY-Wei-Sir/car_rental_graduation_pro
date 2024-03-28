package wsir.carrental.entity.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import wsir.carrental.dict.UserStatus;
import wsir.carrental.dict.UserType;
import wsir.carrental.entity.domain.basic.LogicDelAndVerObject;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("user")
public class User extends LogicDelAndVerObject {
    private String imageId;
    private String identityCard;
    private String frontSideImage;
    private String reverseSideImage;
    private String email;
    private String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String telephone;
    private BigDecimal balance;
    private UserStatus status;
    private UserType userType;
    private Boolean rentCar;
}
