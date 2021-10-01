package pro.inmost.amazon.chime.model.mapper;

import pro.inmost.amazon.chime.model.dto.ContactsDTO;
import pro.inmost.amazon.chime.model.entity.Contacts;


public class ContactDTOMapper {

    public static ContactsDTO map(Contacts contacts) {

        return contacts == null ? null :
                ContactsDTO.builder()
                        .id(contacts.getId())
                        .userName(contacts.getUser().getUsername())
                        .isMuted(contacts.getIsMuted())
                        .isFavorite(contacts.getIsFavorite())
                        .userArn(contacts.getUserArn())
                        .build();
    }

}
