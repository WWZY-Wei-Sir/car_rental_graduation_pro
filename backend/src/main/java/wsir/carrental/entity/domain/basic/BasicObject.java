package wsir.carrental.entity.domain.basic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.beans.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class BasicObject {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Transient
    public Boolean isNew() {
        return this.id == null;
    }
}
