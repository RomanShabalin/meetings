package se.rocketscien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rocketscien.entity.Meeting;
import se.rocketscien.repository.MeetingDAO;
import se.rocketscien.repository.MeetingDAOImpl;
import se.rocketscien.repository.MeetingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingDAO meetingDAO;

    //@Autowired
    public MeetingService(MeetingRepository meetingRepository, MeetingDAO meetingDAO) {
        this.meetingRepository = meetingRepository;
        this.meetingDAO = meetingDAO;
    }

    public List<Meeting> findAllMeetings() {
        return meetingRepository.findAll();
    }

    public List<Meeting> findAllMeetingsWith(String theme, int division, LocalDate startDate, LocalDate endDate, int employee) {
        return meetingDAO.findAllMeetingsWithPredicates(theme, division, startDate, endDate, employee);
    }

    public void deleteById(int id) {
        meetingRepository.deleteById(id);
    }

    public void save(Meeting meeting) {
        meetingRepository.save(meeting);
    }

    public int getLastId() {
        return meetingRepository.getLastId();
    }
}
