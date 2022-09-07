package se.rocketscien.repository;

import org.springframework.stereotype.Repository;
import se.rocketscien.entity.*;
import se.rocketscien.entity.Employee_;
import se.rocketscien.entity.Meeting_;
import se.rocketscien.entity.Participant_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingDAOImpl implements MeetingDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Meeting> findAllMeetingsWithPredicates(String theme, int division, LocalDate startDate, LocalDate endDate, int employee) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> criteriaQuery = criteriaBuilder.createQuery(Meeting.class);
        Root<Meeting> root = criteriaQuery.from(Meeting.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!theme.equals("")) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.like(root.get(Meeting_.theme).as(String.class), "%" +  theme.toUpperCase() + "%"),
                                                                  criteriaBuilder.like(root.get(Meeting_.theme).as(String.class), "%" +  theme.toLowerCase() + "%"),
                                                                  criteriaBuilder.like(root.get(Meeting_.theme).as(String.class), "%" +  firstCharToUpperCase(theme) + "%"))));
        }

        if (division != 0) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(Meeting_.divisionOrg), division)));
        }

        if (startDate != null && endDate == null) {
            endDate = LocalDate.now().plusYears(10L);
            predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get(Meeting_.time), startDate, endDate)));
        }

        if (startDate == null && endDate != null) {
            startDate = LocalDate.now().minusYears(10L);
            predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get(Meeting_.time), startDate, endDate)));
        }

        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get(Meeting_.time), startDate, endDate)));
        }

        if (employee != 0) {
            List<Participant> meetingsByEmployeeId = findMeetingsByEmployeeId(employee);
            List<Integer> meetingsId = new ArrayList<>();
            meetingsByEmployeeId.forEach(meeting -> meetingsId.add(meeting.getMeetingId()));

            List<Predicate> predicateList = new ArrayList<>();
            meetingsByEmployeeId.forEach(meeting -> {
                Predicate predicate = criteriaBuilder.equal(root.get(Meeting_.id), meeting.getMeetingId());
                predicateList.add(predicate);
            });
            Predicate[] predicateArray = predicateList.toArray(new Predicate[predicateList.size()]);
            predicates.add(criteriaBuilder.and(criteriaBuilder.or(predicateArray)));
        }

        criteriaQuery.select(root);

        if (predicates.size() > 0) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private String firstCharToUpperCase(String str) {
        String firstCharToUpperCase = str.substring(0, 1).toUpperCase();
        String allCharsToLowerCase = str.substring(1).toLowerCase();
        String newStr = firstCharToUpperCase + allCharsToLowerCase;
        return newStr;
    }

    private Employee findEmployeeById(int employeeId) {
        CriteriaBuilder criteriaBuilderEmployee = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQueryEmployee = criteriaBuilderEmployee.createQuery(Employee.class);
        Root<Employee> rootEmployee = criteriaQueryEmployee.from(Employee.class);
        Predicate predicateEmployee = criteriaBuilderEmployee.equal(rootEmployee.get(Employee_.id), employeeId);
        criteriaQueryEmployee.select(rootEmployee);
        criteriaQueryEmployee.where(predicateEmployee);
        return entityManager.createQuery(criteriaQueryEmployee).getSingleResult();
    }

    private List<Participant> findMeetingsByEmployeeId(int employeeId) {
        CriteriaBuilder criteriaBuilderParticipant = entityManager.getCriteriaBuilder();
        CriteriaQuery<Participant> criteriaQueryParticipant = criteriaBuilderParticipant.createQuery(Participant.class);
        Root<Participant> rootParticipant = criteriaQueryParticipant.from(Participant.class);
        Predicate predicateParticipant = criteriaBuilderParticipant.equal(rootParticipant.get(Participant_.employeeId), employeeId);
        criteriaQueryParticipant.select(rootParticipant);
        criteriaQueryParticipant.where(predicateParticipant);
        return entityManager.createQuery(criteriaQueryParticipant).getResultList();
    }
}
