package se.rocketscien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rocketscien.entity.Employee;
import se.rocketscien.repository.EmployeeRepository;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(int id) {
        return employeeRepository.findEmployeeById(id);
    }

    public List<Employee> findAllEmployeesByMeetingId(int meetingId) {
        return employeeRepository.findAllEmployeesByMeetingId(meetingId);
    }

    public List<Employee> findAllEmployeesByDivisionId(int divisionId) {
        return employeeRepository.findAllEmployeesByDivisionId(divisionId);
    }
}
