package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.Report;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportDto extends Report {
    private String userName;
    private String publisherName;
    private String comment;
}
