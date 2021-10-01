package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.User;


public interface UserToAppInstanceUserBinder {

    User bindUserToAppInstanceUser(User user, AppInstanceUser appInstanceUser);
}
