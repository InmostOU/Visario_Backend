package pro.inmost.amazon.chime.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.entity.UserInfo;
import pro.inmost.amazon.chime.service.ContactService;
import pro.inmost.amazon.chime.service.UserService;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/contact")
public class ContactController {

    @Autowired
    private UserService userService;

    public final ContactService contactService;

    @PostMapping("/add")
    @SneakyThrows
    public ResponseEntity<ContactAddResponse> addContact(@RequestBody ContactAddRequest contactAddRequest) {

        contactService.add(contactAddRequest);

        return ResponseEntity.ok(
                ContactAddResponse.builder()
                .timestamp(new Date().getTime())
                .status(200)
                .message("Contact is added successfully")
                .path("/contact/add")
                .build()
        );
    }

    @PostMapping("/delete")
    @SneakyThrows
    public ResponseEntity<ContactDeleteResponse> deleteContact(@RequestBody ContactDeleteRequest contactDeleteRequest) {
        contactService.delete(contactDeleteRequest);

        return ResponseEntity.ok(
                ContactDeleteResponse.builder()
                        .timestamp(new Date().getTime())
                        .status(200)
                        .message("Contact is deleted successfully.")
                        .path("/contact/delete")
                        .build()
        );
    }

    @PostMapping("/getAllContacts")
    public ResponseEntity<ContactDetails> getAllContacts() throws JSONException {
        return ResponseEntity.ok(ContactDetails.builder()
            .status(200)
            .message("Contacts were fetched successfully")
            .data(contactService.getCurrentUserContacts())
        .build());
    }

    @GetMapping("/findUserByUsername")
    public ResponseEntity<ContactDetails> getUsersByUsername(@RequestParam String username) {
        List<ContactDetail> details = contactService.findByUsername(username);

        return ResponseEntity.ok(ContactDetails.builder()
            .status(200)
            .message("Found users by username " + username)
            .data(details)
            .build()
        );
    }

    @PostMapping("/muteContact")
    public void muteContact(@RequestBody ContactAddRequest contactAddRequest) {
        contactService.muteUser(contactAddRequest);
    }

    @PostMapping("/addToFavorite")
    public void addToFavorite(@RequestBody ContactAddRequest contactAddRequest) {
        contactService.addToFavorite(contactAddRequest);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<UserInfo> getUserInfo() {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(
                UserInfo.builder()
                        .username(user.getFirstName() + " " + user.getLastName())
                        .money(0.0)
                        .avatarUrl("https://video-editor.su/images/kak-snimalsya-film-avatar_01.jpg")
                        .build()
        );
    }

    @SneakyThrows
    @PostMapping("/edit")
    public ResponseEntity<ContactEditResponse> contactEdit(@RequestBody ContactEditRequest request) {
        return ResponseEntity.ok(
                contactService.editContact(request)
        );
    }
}
