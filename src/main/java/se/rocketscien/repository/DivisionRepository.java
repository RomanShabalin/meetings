package se.rocketscien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.rocketscien.entity.Division;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Integer> {
    @Query(value = "select * from division where id = :id", nativeQuery = true)
    Division findDivisionById(@Param("id") int id);
}
