package pro.inmost.amazon.chime.service;

import software.amazon.awssdk.services.chime.ChimeClient;

/**
 * Interface supposed to serve an AWS Chime PresignedUrlController instance.
 */
public interface ChimeClientService {
    /**
     * Uses AWS SDK to obtain Amazon Chime PresignedUrlController instance.
     *
     * @return {@link ChimeClient}
     */
    ChimeClient getAmazonChimeClient();
}
