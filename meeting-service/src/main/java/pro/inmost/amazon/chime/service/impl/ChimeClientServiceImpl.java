package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.service.ChimeClientService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;

import javax.annotation.PostConstruct;

@Service
@Configuration
public class ChimeClientServiceImpl implements ChimeClientService {

    private static ChimeClient client;
    @Value("${access.key.id}")
    private String accessKeyId;
    @Value("${secret.access.key}")
    private String secretAccessKey;

    private static ChimeClient getClient() {
        return client;
    }

    @PostConstruct
    public void init() {
        initClient();
    }

    @Override
    public ChimeClient getAmazonChimeClient() {
        return getClient();
    }

    private void initClient() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        client = ChimeClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_1)
                .build();
    }
}
