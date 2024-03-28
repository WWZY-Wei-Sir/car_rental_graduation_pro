package wsir.carrental.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import wsir.carrental.entity.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserPrincipal {
    private User currentUser;
}
