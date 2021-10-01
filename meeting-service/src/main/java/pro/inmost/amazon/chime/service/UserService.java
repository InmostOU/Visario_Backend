package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.entity.User;

public interface UserService {

    String getCurrentUserEmail();

    User getCurrentUser();
}
