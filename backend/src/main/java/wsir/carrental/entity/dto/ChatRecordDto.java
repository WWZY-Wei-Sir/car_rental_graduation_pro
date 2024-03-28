package wsir.carrental.entity.dto;

import lombok.*;
import wsir.carrental.entity.domain.ChatRecord;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChatRecordDto extends ChatRecord {
    private String speakerUserName;
    private String recipientUserName;
}
