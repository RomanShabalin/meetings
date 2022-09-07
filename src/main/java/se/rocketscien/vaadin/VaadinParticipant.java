package se.rocketscien.vaadin;

public class VaadinParticipant {
    private int id;
    private String nameParticipant;
    private int years;
    private String division;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameParticipant() {
        return nameParticipant;
    }

    public void setNameParticipant(String nameParticipant) {
        this.nameParticipant = nameParticipant;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
