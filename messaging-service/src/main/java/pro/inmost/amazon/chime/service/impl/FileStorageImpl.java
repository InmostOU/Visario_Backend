package pro.inmost.amazon.chime.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.service.FileStorage;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStorageImpl implements FileStorage {

    @Value("${s3.bucketName}")
    private String bucketName;

    private S3ClientService clientService;

    @Autowired
    public FileStorageImpl(S3ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public BaseResponse upload(String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });

        clientService.getS3Client().putObject(bucketName, fileName, inputStream, objectMetadata);

        return BaseResponse.standard();
    }

    @Override
    public String getDownloadUrl(String objectKey) {
        String urlToFile = "";

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey)
                    .withMethod(HttpMethod.GET);
        URL url = clientService.getS3Client().generatePresignedUrl(request);
        urlToFile = url.toString();

        return urlToFile;
    }

    @Override
    public BaseResponse delete(String objectKey) {
        return null;
    }
}
