package pro.inmost.amazon.chime.service;

import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Attachment;
import pro.inmost.amazon.chime.model.entity.Message;
import pro.inmost.amazon.chime.model.entity.User;

import java.io.IOException;
import java.util.Optional;

public interface AttachmentService {

    Attachment uploadAttachment(MultipartFile file, String prefix) throws IOException;

    String getAttachmentUrl(Attachment attachment);

    Integer getOcuppiedSpaceSize(AppInstanceUser user);

    Boolean isUserReachedLimit(AppInstanceUser user, Integer limit);

    Optional<String> getFileType(String filename);

    Optional<String> getFileName(String filename);

     Optional<String> getEncodedURL(String URL);
}
