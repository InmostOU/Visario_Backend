package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;
import pro.inmost.amazon.chime.model.entity.User;

/**
 * Creates a user under the specified Amazon Chime account.
 * <a href="#{@https://docs.aws.amazon.com/chime/latest/APIReference/API_CreateUser.html}">More detail</a>.
 */

public interface ChimeAppInstanceUserService {
    /**
     * @param chimeAccount      the object from which we will obtain the <a href="#{@https://docs.aws.amazon.com/chime/latest/APIReference/API_CreateUser.html}">accountId</a>.
     * @param username          the username.
     * @param appInstanceArn    the ARN of the AppInstance request.
     * @param appInstanceUserId The user ID of the AppInstance. We must give this id by ourself.
     * @return                  an object for {@link AppInstanceUser}.
     */
    AppInstanceUser createUser(ChimeAccount chimeAccount,  String username, String appInstanceArn, User appInstanceUserId);

}
