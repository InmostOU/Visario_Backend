package pro.inmost.amazon.chime.service.impl;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.AmazonChimeClientBuilder;
import com.amazonaws.services.chime.model.Attendee;
import com.amazonaws.services.chime.model.CreateAttendeeRequest;
import com.amazonaws.services.chime.model.CreateAttendeeResult;
import com.amazonaws.services.chime.model.CreateMeetingRequest;
import com.amazonaws.services.chime.model.CreateMeetingResult;
import com.amazonaws.services.chime.model.DeleteAttendeeRequest;
import com.amazonaws.services.chime.model.DeleteMeetingRequest;
import com.amazonaws.services.chime.model.Meeting;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.config.AwsMeetingClient;
import pro.inmost.amazon.chime.exceptions.MeetingNotFoundException;
import pro.inmost.amazon.chime.model.dto.MeetingAndUserIdDTO;
import pro.inmost.amazon.chime.model.dto.MeetingDTO;
import pro.inmost.amazon.chime.model.dto.UserInfoForMeeting;
import pro.inmost.amazon.chime.model.entity.AttendeeEntity;
import pro.inmost.amazon.chime.model.entity.MeetingEntity;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repository.AttendeeEntityRepository;
import pro.inmost.amazon.chime.repository.MeetingEntityRepository;
import pro.inmost.amazon.chime.repository.UserRepository;
import pro.inmost.amazon.chime.service.MeetingService;
import pro.inmost.amazon.chime.service.UserService;
import software.amazon.awssdk.services.chime.model.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final MeetingEntityRepository meetingEntityRepository;
    private final AWSCredentialsProvider awsCredentialsProvider;
    private final AttendeeEntityRepository attendeeEntityRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private AmazonChime meetingClient = AwsMeetingClient.getMeetingClient();

    public MeetingServiceImpl(MeetingEntityRepository meetingEntityRepository,
                              AWSCredentialsProvider awsCredentialsProvider,
                              AttendeeEntityRepository attendeeEntityRepository,
                              UserService userService, UserRepository userRepository) {
        this.meetingEntityRepository = meetingEntityRepository;
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.attendeeEntityRepository = attendeeEntityRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    public MeetingDTO createNewMeetingInfo() {

        //TODO update external meeting ID
        User currentUser = userService.getCurrentUser();
        UUID uuid = UUID.randomUUID();

        AmazonChime chime = AmazonChimeClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        CreateMeetingRequest createMeetingRequest = new CreateMeetingRequest();

        createMeetingRequest.setClientRequestToken(uuid.toString());
        createMeetingRequest.setExternalMeetingId(UUID.randomUUID().toString());
        createMeetingRequest.setMediaRegion("us-east-1");
        createMeetingRequest.setMeetingHostId("visario");

        CreateMeetingResult meetingResult = chime.createMeeting(createMeetingRequest);
        Meeting meeting = meetingResult.getMeeting();

        Attendee attendee = createAndSaveAttendee(currentUser.getId().toString(), meeting.getMeetingId(), chime, meeting);

        MeetingEntity meetingEntity = new MeetingEntity();
        meetingEntity.setMeeting(meeting);
        meetingEntity.setUserId(currentUser.getId());
        meetingEntity.setCreateDate(new Date());
        meetingEntity.setMeetingId(meeting.getMeetingId());
        meetingEntityRepository.save(meetingEntity);

        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setMeetingObject(meeting);
        meetingDTO.setAttendeeObject(attendee);

        return meetingDTO;
    }

    @Override
    public MeetingDTO getMeeting(String meetingId) throws MeetingNotFoundException {
        User currentUser = userService.getCurrentUser();
        MeetingDTO meetingDTO = null;
        if (meetingEntityRepository.findByMeetingIdAndUserId(meetingId, currentUser.getId()).isPresent()) {
            meetingDTO = setMeetingAndAttendee(meetingId, currentUser, false);
        } else if (!meetingEntityRepository.findByMeetingIdAndUserId(meetingId, currentUser.getId()).isPresent()) {
            meetingDTO = setMeetingAndAttendee(meetingId, currentUser, true);
        } else throw new MeetingNotFoundException("Cannot find meeting with id " + meetingId);
        return meetingDTO;
    }

    private MeetingDTO setMeetingAndAttendee(String meetingId, User currentUser, boolean ifNotExist) {
        MeetingDTO meetingDTO = new MeetingDTO();
        List<MeetingEntity> meetingEntity = meetingEntityRepository.findByMeetingId(meetingId);
        AmazonChime chime = AmazonChimeClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        Meeting meeting = meetingEntity.get(0).getMeeting();

        MeetingEntity newMeetingEntity = new MeetingEntity();
        newMeetingEntity.setMeeting(meeting);
        newMeetingEntity.setUserId(currentUser.getId());
        newMeetingEntity.setCreateDate(new Date());
        newMeetingEntity.setMeetingId(meeting.getMeetingId());

        if (ifNotExist) meetingEntityRepository.save(newMeetingEntity);

        Attendee attendee = createAndSaveAttendee(currentUser.getId().toString(), meeting.getMeetingId(), chime, meeting);
        meetingDTO.setMeetingObject(newMeetingEntity.getMeeting());
        meetingDTO.setAttendeeObject(attendee);
        return meetingDTO;
    }

    private Attendee createAndSaveAttendee(String userId, String meetingId, AmazonChime chime, Meeting meeting) {

        CreateAttendeeRequest createAttendeeRequest = new CreateAttendeeRequest().withMeetingId(meetingId);
        createAttendeeRequest.setExternalUserId(userId);
        CreateAttendeeResult createAttendeeResult = chime.createAttendee(createAttendeeRequest);

        Attendee attendee = createAttendeeResult.getAttendee();
        AttendeeEntity attendeeEntity = new AttendeeEntity();
        attendeeEntity.setAttendee(attendee);
        attendeeEntity.setMeetingId(meeting.getMeetingId());
        attendeeEntity.setUserId(userId);
        attendeeEntityRepository.save(attendeeEntity);
        return attendee;
    }

    @Override
    public UserInfoForMeeting getUserInfoForMeetingByUserId(MeetingAndUserIdDTO meetingAndUserIdDTO) throws MeetingNotFoundException {
        if (meetingEntityRepository.findByMeetingIdAndUserId(meetingAndUserIdDTO.getMeetingId(),
                Long.parseLong(meetingAndUserIdDTO.getUserId())).isPresent()) {
            Optional<User> user = userRepository.findById(Long.parseLong(meetingAndUserIdDTO.getUserId()));
            return UserInfoForMeeting.builder()
                    .id(user.get().getId())
                    .firstName(user.get().getFirstName())
                    .lastName(user.get().getLastName())
                    .image(user.get().getAvatar() != null ? user.get().getAvatar() : "")
                    .build();
        } else throw new MeetingNotFoundException("can't find meeting with id " + meetingAndUserIdDTO.getMeetingId()
                + "or user is not from meeting");
    }

    @Override
    public void deleteMeeting(String meetingId) {
        DeleteMeetingRequest request = new DeleteMeetingRequest()
                .withMeetingId(meetingId)
                .withRequestCredentialsProvider(awsCredentialsProvider);
        meetingEntityRepository.deleteByMeetingId(meetingId);
        meetingClient.deleteMeeting(request);
    }

    @Override
    public void deleteAttendee(MeetingAndUserIdDTO meetingAndUserIdDTO) throws NotFoundException, MeetingNotFoundException {
        if (attendeeEntityRepository.findByMeetingIdAndUserId(meetingAndUserIdDTO.getMeetingId(), meetingAndUserIdDTO.getUserId()).isPresent()) {
            AttendeeEntity attendeeEntity = attendeeEntityRepository.findByMeetingIdAndUserId(meetingAndUserIdDTO.getMeetingId(), meetingAndUserIdDTO.getUserId()).get();
            DeleteAttendeeRequest request = new DeleteAttendeeRequest().withMeetingId(meetingAndUserIdDTO.getMeetingId())
                    .withAttendeeId(attendeeEntity.getAttendee().getAttendeeId())
                    .withRequestCredentialsProvider(awsCredentialsProvider);
            meetingClient.deleteAttendee(request);
            attendeeEntityRepository.deleteByMeetingIdAndUserId(meetingAndUserIdDTO.getMeetingId(), meetingAndUserIdDTO.getUserId());
        } else throw new MeetingNotFoundException("cant find meeting with id " + meetingAndUserIdDTO.getMeetingId());
    }
}
