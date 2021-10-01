package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.regions.Region;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.service.ChimeClientService;
import javax.annotation.PostConstruct;

@Service
public class ChimeClientServiceImpl implements ChimeClientService {

    @Value("${access.key.id}")
    private String accessKeyId;

    @Value("${secret.access.key}")
    private String secretAccessKey;

    private static ChimeClient client;

    @PostConstruct
    public void init() {
        initClient();
    }

    /**
     * Uses AWS SDK to obtain Amazon Chime Client instance.
     *
     * @return {@link ChimeClient}
     */
    @Override
    public ChimeClient getAmazonChimeClient() {
        return getClient();
    }

    private static ChimeClient getClient() {
        return client;
    }

    private void initClient() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        client = ChimeClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_1)
                .build();
    }
}
