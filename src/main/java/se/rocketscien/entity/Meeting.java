package se.rocketscien.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "theme")
    private String theme;

    @Column(name = "time")
    private LocalDate time;

    @Column(name = "division_org")
    private int divisionOrg;

    @Column(name = "responsible")
    private int responsible;

    @ManyToMany(mappedBy = "meetings", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employee> employees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public int getDivisionOrg() {
        return divisionOrg;
    }

    public void setDivisionOrg(int divisionOrg) {
        this.divisionOrg = divisionOrg;
    }

    public int getResponsible() {
        return responsible;
    }

    public void setResponsible(int responsible) {
        this.responsible = responsible;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
