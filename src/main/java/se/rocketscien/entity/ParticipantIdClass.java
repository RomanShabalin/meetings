package se.rocketscien.entity;

import java.io.Serializable;

public class ParticipantIdClass implements Serializable {
    private int meetingId;
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
