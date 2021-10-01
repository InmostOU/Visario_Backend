package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.exeption.ContactNotFoundException;
import pro.inmost.amazon.chime.exeption.UserAlreadyAddException;
import pro.inmost.amazon.chime.exeption.UserNotFoundException;
import pro.inmost.amazon.chime.model.dto.*;

import java.util.List;

public interface ContactService {
    void add(ContactAddRequest contactAddRequest) throws UserNotFoundException, UserAlreadyAddException;

    void delete(ContactDeleteRequest request) throws ContactNotFoundException;

    List<ContactDetail> getCurrentUserContacts();

    void muteUser(ContactAddRequest contactAddRequest);

    void addToFavorite(ContactAddRequest contactAddRequest);

    List<ContactDetail> findByUsername(String username);

    ContactEditResponse editContact(ContactEditRequest contactEditRequest) throws ContactNotFoundException;
}

