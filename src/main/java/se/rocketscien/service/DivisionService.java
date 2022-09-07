package se.rocketscien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rocketscien.entity.Division;
import se.rocketscien.repository.DivisionRepository;

import java.util.List;

@Service
@Transactional
public class DivisionService {
    private final DivisionRepository divisionRepository;

    @Autowired
    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    public List<Division> findAllDivisions() {
        return divisionRepository.findAll();
    }

    public Division findDivisionById(int id) {
        return divisionRepository.findDivisionById(id);
    }
}
