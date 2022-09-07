package se.rocketscien.utils;

import se.rocketscien.vaadin.VaadinMeeting;

public class Utils {
    private static VaadinMeeting vaadinMeeting;

    public static VaadinMeeting getVaadinMeeting() {
        return vaadinMeeting;
    }

    public static void setVaadinMeeting(VaadinMeeting vaadinMeeting) {
        Utils.vaadinMeeting = vaadinMeeting;
    }
}
