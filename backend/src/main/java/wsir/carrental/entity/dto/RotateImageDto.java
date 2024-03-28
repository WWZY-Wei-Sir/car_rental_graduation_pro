package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.RotateImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RotateImageDto extends RotateImage {
    private String url;
}
