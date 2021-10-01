package pro.inmost.amazon.chime.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.AmazonChimeClient;

public class AwsMeetingClient {

    public static AmazonChime getMeetingClient() {
        return AmazonChimeClient.builder()
                .withCredentials(new AWSCredentialsBasic())
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
