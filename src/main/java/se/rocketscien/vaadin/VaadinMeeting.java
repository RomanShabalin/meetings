package se.rocketscien.vaadin;

import java.time.LocalDate;

public class VaadinMeeting {
    private int id;
    private String theme;
    private LocalDate time;
    private String divisionOrg;
    private int divisionOrgId;
    private String responsible;
    private String responsibleFullName;
    private int responsibleId;
    private int participantCount;

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

    public String getDivisionOrg() {
        return divisionOrg;
    }

    public void setDivisionOrg(String divisionOrg) {
        this.divisionOrg = divisionOrg;
    }

    public int getDivisionOrgId() {
        return divisionOrgId;
    }

    public void setDivisionOrgId(int divisionOrgId) {
        this.divisionOrgId = divisionOrgId;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getResponsibleFullName() {
        return responsibleFullName;
    }

    public void setResponsibleFullName(String responsibleFullName) {
        this.responsibleFullName = responsibleFullName;
    }

    public int getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(int responsibleId) {
        this.responsibleId = responsibleId;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
}
