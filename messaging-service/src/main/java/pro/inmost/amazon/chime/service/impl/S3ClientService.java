package pro.inmost.amazon.chime.service.impl;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import javax.annotation.PostConstruct;

@Service
public class S3ClientService {

    @Value("${s3.accessKey}")
    private String accessKeyId;

    @Value("${s3.secretKey}")
    private String secretAccessKey;

    @Value("${s3.region}")
    private String region;

    private static AmazonS3 client;

    @PostConstruct
    public void init() {
        initClient();
    }

    public AmazonS3 getS3Client() {
        return getClient();
    }

    private static AmazonS3 getClient() {
        return client;
    }

    private void initClient() {

        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));

        client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials)
                .withRegion(region)
                .build();
    }

}
