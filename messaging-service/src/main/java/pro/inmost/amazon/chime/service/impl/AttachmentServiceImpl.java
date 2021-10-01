package pro.inmost.amazon.chime.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Attachment;
import pro.inmost.amazon.chime.model.entity.Message;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repository.AttachmentRepository;
import pro.inmost.amazon.chime.service.AttachmentService;
import pro.inmost.amazon.chime.service.FileStorage;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private static Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Value("${s3.maxSizeForFileBytes}")
    private Long maxSizeForFileBytes;

    @Value("${s3.maxAvailableSpaceBytes}")
    private Long maxAvailableSpaceBytes;

    private FileStorage fileStorage;
    private AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentServiceImpl(FileStorage fileStorage, AttachmentRepository attachmentRepository) {
        this.fileStorage = fileStorage;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment uploadAttachment(MultipartFile file, String prefix) throws IOException {

        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String randomUUID = UUID.randomUUID().toString();

        fileStorage.upload(prefix + "/" + randomUUID + "-" + file.getOriginalFilename(), Optional.of(metadata), file.getInputStream());

        return attachmentRepository.save(
                Attachment.builder()
                        .fileName(randomUUID + "-" + getFileName(file.getOriginalFilename()).orElse("unknown"))
                        .fileType(getFileType(file.getOriginalFilename()).orElse("unknown"))
                        .fileSize(file.getSize())
                        .prefix(prefix)
                        .build()
        );
    }

    @Override
    public String getAttachmentUrl(Attachment attachment) {
        return fileStorage.getDownloadUrl(attachment.getPrefix() + "/" + attachment.getFileName() + "." + attachment.getFileType());
    }

    @Override
    public Integer getOcuppiedSpaceSize(AppInstanceUser user) {
        return null;
    }

    @Override
    public Boolean isUserReachedLimit(AppInstanceUser user, Integer limit) {
        return null;
    }

    public Optional<String> getFileType(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    @Override
    public Optional<String> getEncodedURL(String URL) {
        return Optional.empty();
    }

    public Optional<String> getFileName(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(0, f.lastIndexOf(".")));
    }


}
