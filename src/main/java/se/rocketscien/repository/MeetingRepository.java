package se.rocketscien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.rocketscien.entity.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    @Query(value = "delete from meeting where id = :id", nativeQuery = true)
    @Modifying
    void deleteById(@Param("id") int id);

    @Query(value = "select max(id) from meeting", nativeQuery = true)
    int getLastId();
}
