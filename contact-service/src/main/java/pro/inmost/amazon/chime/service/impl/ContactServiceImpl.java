package pro.inmost.amazon.chime.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.exeption.ContactNotFoundException;
import pro.inmost.amazon.chime.exeption.UserAlreadyAddException;
import pro.inmost.amazon.chime.exeption.UserNotFoundException;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Contacts;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.entity.UserAvatar;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.repository.ContactsRepository;
import pro.inmost.amazon.chime.repository.UserAvatarRepository;
import pro.inmost.amazon.chime.repository.UserRepository;
import pro.inmost.amazon.chime.service.ContactService;
import pro.inmost.amazon.chime.service.UserService;

import java.util.*;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactsRepository contactsRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final UserAvatarRepository userAvatarRepository;

    @Override
    public void add(ContactAddRequest contactAddRequest) throws UserNotFoundException, UserAlreadyAddException {
        long id = userService.findUserByEmail(userService.getCurrentUserEmail()).getId();
        Set<Contacts> contactsList = contactsRepository.findAllByOwnerId(id);
        Map<String, Long> map = new HashMap<>();

        for (Contacts ct : contactsList) {
            map.put(ct.getUser().getUsername(), ct.getOwnerId());
        }
        if (map.containsKey(contactAddRequest.getUsername()))
            throw new UserAlreadyAddException("The user has already been added to contacts");

        User user = userService.getCurrentUser();

        Contacts contacts = new Contacts();
        contacts.setOwnerId(user.getId());
        contacts.setIsMuted(false);
        contacts.setIsFavorite(false);
        User userContact = userRepository.findByUsername(contactAddRequest.getUsername());
        if (userContact == null)
            throw new UserNotFoundException("User with username" + contactAddRequest.getUsername() + "is not found.");
        AppInstanceUser appInstanceUser = appInstanceUserRepository.findByUser(userContact);
        contacts.setUser(userContact);
        contacts.setUserArn(appInstanceUser.getAppInstanceUserArn());
        contacts.setFirstName(userContact.getFirstName());
        contacts.setLastName(userContact.getLastName());
        contactsRepository.save(contacts);
    }

    @Override
    public void delete(ContactDeleteRequest request) throws ContactNotFoundException {
        contactsRepository.findById(request.getId())
                .orElseThrow(() -> new ContactNotFoundException("Contact not found. Path: {/contact/delete}"));

        contactsRepository.deleteById(request.getId());
    }

    @Override
    public List<ContactDetail> getCurrentUserContacts() {

        long id = userService.getCurrentUser().getId();
        Set<Contacts> contacts = contactsRepository.findAllByOwnerId(id);
        List<ContactDetail> contactDetails = new ArrayList<>();

        for (Contacts contact : contacts) {

            User user = contact.getUser();
            String phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
            String email = user.getEmail() != null ? user.getEmail() : "";
            UserAvatar[] avatar = new UserAvatar[1];

            userAvatarRepository.findByUser(user).ifPresent(userAvatar -> {
                avatar[0] = userAvatar;
            });

            contactDetails.add(ContactDetail.builder()
                    .id(contact.getId())
                    .userArn(contact.getUserArn())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .username(contact.getUser().getUsername())
                    .email((user.getShowEmailTo().equals("EVERYONE") || user.getShowEmailTo().equals("CONTACTS")) ? email : "")
                    .phoneNumber((user.getShowPhoneNumberTo().equals("EVERYONE") || user.getShowPhoneNumberTo().equals("CONTACTS")) ? phoneNumber : "")
                    .image(avatar[0] != null ? userService.getUserAvatarUrl(avatar[0].getUser().getId()) : "")
                    .about(contact.getUser().getAbout() != null ? contact.getUser().getAbout() : "")
                    .online(contact.getUser().getOnline())
                    .favorite(contact.getIsFavorite())
                    .muted(contact.getIsMuted())
                    .inMyContacts(true)
                    .build()
            );
        }


        return contactDetails;
    }

    @Override
    public void muteUser(ContactAddRequest contactAddRequest) {
        long id = userService.getCurrentUser().getId();
        Contacts contacts = contactsRepository.findByOwnerIdAndUserId(id,
                userRepository.findByUsername(contactAddRequest.getUsername()).getId());
        boolean isMuted = contacts.getIsMuted();
        contacts.setIsMuted(!isMuted);
        contactsRepository.save(contacts);
    }

    @Override
    public void addToFavorite(ContactAddRequest contactAddRequest) {
        long id = userService.getCurrentUser().getId();
        Contacts contacts = contactsRepository.findByOwnerIdAndUserId(id,
                userRepository.findByUsername(contactAddRequest.getUsername()).getId());
        boolean isFavorite = contacts.getIsFavorite();
        contacts.setIsMuted(!isFavorite);
        contactsRepository.save(contacts);
    }

    @Override
    public List<ContactDetail> findByUsername(String username) {

        if (username == null || username.isEmpty())
            return new ArrayList<>();

        long currentUserId = userService.getCurrentUser().getId();
        Set<Contacts> contacts = contactsRepository.findAllByOwnerId(currentUserId);
        List<User> foundUsers = userRepository.findUsersByUsername(username);
        List<ContactDetail> userDetails = new ArrayList<>();

        if (foundUsers != null) {
            for (User user : foundUsers) {

                if (user.getIsActive() == true) {
                    AppInstanceUser appInstanceUser = appInstanceUserRepository.findByUser(user);
                    Contacts contact = contactsRepository.findByOwnerIdAndUserId(currentUserId, user.getId());
                    String phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
                    String email = user.getEmail();
                    final UserAvatar[] avatar = new UserAvatar[1];
                    userAvatarRepository.findByUser(user).ifPresent(userAvatar -> avatar[0] =  userAvatar);

                    userDetails.add(ContactDetail.builder()
                            .id(user.getId())
                            .userArn(appInstanceUser != null ? appInstanceUser.getAppInstanceUserArn() : "")
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .username(user.getUsername())
                            .email(user.getShowEmailTo().equals("EVERYONE") ? email : "")
                            .phoneNumber(user.getShowPhoneNumberTo().equals("EVERYONE") ? phoneNumber : "")
                            .image(avatar[0] != null ? userService.getUserAvatarUrl(avatar[0].getUser().getId()) : "")
                            .about(user.getAbout() != null ? user.getAbout() : "")
                            .online(user.getOnline())
                            .favorite(contact != null ? contact.getIsFavorite() : false)
                            .muted(contact != null ? contact.getIsMuted() : false)
                            .inMyContacts(contacts.contains(contact) ? true : false)
                            .build()
                    );
                }
            }
        }

        return userDetails != null ? userDetails : new ArrayList<>();
    }

    @Override
    public ContactEditResponse editContact(ContactEditRequest contactEditRequest) throws ContactNotFoundException {

        Contacts contact = contactsRepository.findById(contactEditRequest.getId()).orElseThrow(
                () -> new ContactNotFoundException("Contact not found. Path: {/contact/edit}")
        );

        contact.setFirstName(contactEditRequest.getFirstName());
        contact.setLastName(contactEditRequest.getLastName());
        contactsRepository.save(contact);

        return ContactEditResponse.builder()
                .message("Contact has been edited successfully")
                .timestamp(new Date().getTime())
                .status(200)
                .path("/contact/edit")
                .build();
    }

}