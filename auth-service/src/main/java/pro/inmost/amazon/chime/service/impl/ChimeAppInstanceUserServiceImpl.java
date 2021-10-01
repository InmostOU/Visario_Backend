package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repositories.AppInstanceUserRepository;
import pro.inmost.amazon.chime.service.ChimeAppInstanceUserService;
import pro.inmost.amazon.chime.service.ChimeClientService;
import software.amazon.awssdk.services.chime.model.*;

@Service
public class ChimeAppInstanceUserServiceImpl implements ChimeAppInstanceUserService {

    @Autowired
    private AppInstanceUserRepository repository;

    @Autowired
    private ChimeClientService chimeClientService;

    /**
     * @param chimeAccount    the object from which we will obtain the <a href="#{@https://docs.aws.amazon.com/chime/latest/APIReference/API_CreateUser.html}">accountId</a>.
     * @param username        the username.
     * @param appInstanceArn  the ARN of the AppInstance request.
     * @return an object for {@link AppInstanceUser}.
     */
    @Override
    public AppInstanceUser createUser(ChimeAccount chimeAccount, String username, String appInstanceArn, User appInstanceUserId) {
        CreateAppInstanceUserRequest appInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId(appInstanceUserId.getId().toString())
                .appInstanceArn(appInstanceArn)
                .name(username)
                .build();

        CreateAppInstanceUserResponse appInstanceResponse = chimeClientService.getAmazonChimeClient()
                .createAppInstanceUser(appInstanceUserRequest);

        AppInstanceUser user = AppInstanceUser.builder()
                .appInstanceUserArn(appInstanceResponse.appInstanceUserArn())
                .appInstanceArn(appInstanceArn)
                .user(appInstanceUserId)
                .chimeAccount(chimeAccount)
                .build();

        repository.save(user);
        return user;
    }
}
