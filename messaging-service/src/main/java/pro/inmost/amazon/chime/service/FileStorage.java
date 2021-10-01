package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.dto.BaseResponse;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileStorage {

    BaseResponse upload(String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream);

    String getDownloadUrl(String objectKey);

    BaseResponse delete(String objectKey);
}
