package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class FileUrlDTO {
    private String fileUrl;
    private long expTimeMillis;
}

