package se.rocketscien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rocketscien.entity.Participant;
import se.rocketscien.repository.ParticipantRepository;

@Service
@Transactional
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public int getCountParticipantByMeetingId(int meetingId) {
        return participantRepository.getCountParticipantByMeetingId(meetingId);
    }

    public void deleteByMeetingId(int id) {
        participantRepository.deleteByMeetingId(id);
    }

    public void save(Participant participant) {
        participantRepository.save(participant);
    }
}
