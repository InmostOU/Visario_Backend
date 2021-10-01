package pro.inmost.amazon.chime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private String channelArn;
    private String content;
    private Metadata metadata;
}
