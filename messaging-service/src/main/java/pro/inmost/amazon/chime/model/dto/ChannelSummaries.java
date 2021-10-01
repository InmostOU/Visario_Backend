package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ChannelSummaries {
    private int status;
    private String message;
    private List<ChannelSummary> data;
}
