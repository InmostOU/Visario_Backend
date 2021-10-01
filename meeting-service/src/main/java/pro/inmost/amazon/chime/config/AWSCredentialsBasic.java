package pro.inmost.amazon.chime.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSCredentialsBasic implements AWSCredentialsProvider {
    @Value("${secret.access.key}")
    private String accessKey;

    @Value("${access.key.id}")
    private String secretKey;

    public AWSCredentials getCredentials() {
        return new BasicAWSCredentials(secretKey, accessKey);
    }

    @Override
    public void refresh() {

    }
}
