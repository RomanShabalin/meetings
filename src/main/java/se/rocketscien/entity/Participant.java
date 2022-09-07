package se.rocketscien.entity;

import javax.persistence.*;

@Entity
@Table(name = "participant")
@IdClass(ParticipantIdClass.class)
public class Participant {
    @Id
    @Column(name = "meeting_id")
    private int meetingId;

    @Id
    @Column(name = "employee_id")
    private int employeeId;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
