package se.rocketscien.repository;

import org.springframework.stereotype.Repository;
import se.rocketscien.entity.Meeting;

import java.time.LocalDate;
import java.util.List;

public interface MeetingDAO {
    List<Meeting> findAllMeetingsWithPredicates(String theme, int division, LocalDate startDate, LocalDate endDate, int employee);
}
