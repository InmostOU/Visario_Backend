package pro.inmost.amazon.chime.service;

import software.amazon.awssdk.services.chime.ChimeClient;

/**
 * Interface supposed to serve an AWS Chime Client instance.
 */
public interface ChimeClientService {
    /**
     * Uses AWS SDK to obtain Amazon Chime Client instance.
     *
     * @return {@link ChimeClient}
     */
    ChimeClient getAmazonChimeClient();
}
