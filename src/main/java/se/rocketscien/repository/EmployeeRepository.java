package se.rocketscien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.rocketscien.entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "select * from employee where id = :id", nativeQuery = true)
    Employee findEmployeeById(@Param("id") int id);

    @Query(value = "select e.id, e.name, e.birth_date, e.division_id from employee e\n" +
            "    join participant p on e.id = p.employee_id\n" +
            "    join meeting m on m.id = p.meeting_id\n" +
            "where m.id = :meetingId order by id", nativeQuery = true)
    List<Employee> findAllEmployeesByMeetingId(@Param("meetingId") int meetingId);

    @Query(value = "select * from employee e\n" +
            "join division d on d.id = e.division_id\n" +
            "where e.division_id = :divisionId", nativeQuery = true)
    List<Employee> findAllEmployeesByDivisionId(@Param("divisionId") int divisionId);
}
