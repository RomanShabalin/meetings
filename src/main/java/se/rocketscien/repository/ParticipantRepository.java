package se.rocketscien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.rocketscien.entity.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    @Query(value = "select count(employee_id) from participant where meeting_id = :meetingId", nativeQuery = true)
    int getCountParticipantByMeetingId(@Param("meetingId") int meetingId);

    @Query(value = "delete from participant where meeting_id = :id", nativeQuery = true)
    @Modifying
    void deleteByMeetingId(@Param("id") int id);
}
