package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.model.dto.ConversationSummaries;
import pro.inmost.amazon.chime.model.dto.CreateConversationRequest;

public interface ConversationService {

     BaseResponse createConversation(CreateConversationRequest request);

     ConversationSummaries fetchConversations();

     BaseResponse inviteToConversation(String channelArn, String firstParticipantArn, String secondParticipantArn);

}
