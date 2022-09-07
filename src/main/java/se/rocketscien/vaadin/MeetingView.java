package se.rocketscien.vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import se.rocketscien.entity.Division;
import se.rocketscien.entity.Employee;
import se.rocketscien.entity.Meeting;
import se.rocketscien.service.DivisionService;
import se.rocketscien.service.EmployeeService;
import se.rocketscien.service.MeetingService;
import se.rocketscien.service.ParticipantService;
import se.rocketscien.utils.Utils;

import java.time.LocalDate;
import java.util.*;

@Route("/meeting")
@PageTitle("Meetings")
public class MeetingView extends VerticalLayout {
    private final DivisionService divisionService;
    private final EmployeeService employeeService;
    private final MeetingService meetingService;
    private final ParticipantService participantService;

    private List<Division> divisionList;
    private List<Employee> employeeList;
    private Map<String, String> employeeMap;
    private List<Meeting> meetings;

    private TextField themeSearch;
    private ComboBox<String> chooseDivision;
    private DatePicker.DatePickerI18n startDateI18n;
    private DatePicker startDate;
    private DatePicker.DatePickerI18n endDateI18n;
    private DatePicker endDate;
    private ComboBox<String> chooseEmployee;
    private HorizontalLayout searchLayout;

    private Button findTheme;
    private Button clearSearchFields;
    private HorizontalLayout buttonSearchLayout;

    private Grid<VaadinMeeting> meetingGrid;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private HorizontalLayout buttonLayout;

    public MeetingView(DivisionService divisionService, EmployeeService employeeService, MeetingService meetingService, ParticipantService participantService) {
        this.divisionService = divisionService;
        this.employeeService = employeeService;
        this.meetingService = meetingService;
        this.participantService = participantService;

        meetings = meetingService.findAllMeetings();

        themeSearch = new TextField("Enter the theme:");
        chooseDivision = new ComboBox<>("Choose the division:");
        startDateI18n = new DatePicker.DatePickerI18n();
        startDate = new DatePicker("Select the start date:");
        endDateI18n = new DatePicker.DatePickerI18n();
        endDate = new DatePicker("Select the end date:");
        chooseEmployee = new ComboBox<>("Choose the employee:");
        searchLayout = new HorizontalLayout(themeSearch, chooseDivision, startDate, endDate, chooseEmployee);

        findTheme = new Button("Find themes", new Icon(VaadinIcon.SEARCH), event -> findThemeListener());
        findTheme.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        clearSearchFields = new Button("Clear search fields", new Icon(VaadinIcon.ERASER), event -> clearSearchFieldsListener());
        buttonSearchLayout = new HorizontalLayout(findTheme, clearSearchFields);

        buttonSearchLayout.setSizeFull();
        buttonSearchLayout.setAlignItems(Alignment.CENTER);
        buttonSearchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        themeSearch.setPlaceholder("theme");
        themeSearch.setWidth("300px");

        chooseDivision.setPlaceholder("division");
        chooseDivision.setWidth("200px");
        divisionList = this.divisionService.findAllDivisions();
        List<String> divisions = new ArrayList<>();
        divisionList.forEach(division -> divisions.add(division.getName()));
        chooseDivision.setItems(divisions);

        startDate.setPlaceholder("start date");
        startDateI18n.setDateFormat("yyyy-MM-dd");
        startDate.setI18n(startDateI18n);

        endDate.setPlaceholder("end date");
        endDateI18n.setDateFormat("yyyy-MM-dd");
        endDate.setI18n(endDateI18n);

        chooseEmployee.setPlaceholder("employee");
        chooseEmployee.setWidth("250px");
        employeeList = this.employeeService.findAllEmployees();
        employeeMap = new HashMap<>();
        List<String> employees = new ArrayList<>();
        employeeList.forEach(employee -> {
            employees.add(getShortName(employee));

            employeeMap.put(getShortName(employee), employee.getName());
        });
        chooseEmployee.setItems(employees);

        searchLayout.setSizeFull();
        searchLayout.setAlignItems(Alignment.CENTER);
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        meetingGrid = new Grid<>(VaadinMeeting.class);
        meetingGrid.removeColumnByKey("id");
        meetingGrid.removeColumnByKey("divisionOrgId");
        meetingGrid.removeColumnByKey("responsibleId");
        meetingGrid.removeColumnByKey("responsibleFullName");
        meetingGrid.setColumnOrder(meetingGrid.getColumnByKey("theme"),
                                   meetingGrid.getColumnByKey("time"),
                                   meetingGrid.getColumnByKey("divisionOrg"),
                                   meetingGrid.getColumnByKey("responsible"),
                                   meetingGrid.getColumnByKey("participantCount"));
        meetingGrid.setItems(this.getAllMeetings(meetings));
        meetingGrid.setSortableColumns("time");
        meetingGrid.setWidthFull();
        meetingGrid.addSelectionListener(e -> gridClickListener());

        addButton = new Button("Add", new Icon(VaadinIcon.INSERT), event -> UI.getCurrent().navigate("/data"));
        editButton = new Button("Edit", new Icon(VaadinIcon.EDIT), event -> editButtonListener());
        deleteButton = new Button("Delete", new Icon(VaadinIcon.DEL), event -> createNotification().open());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        addButton.setWidth("150px");
        editButton.setWidth("150px");
        deleteButton.setWidth("150px");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonLayout = new HorizontalLayout(addButton, editButton, deleteButton);
        buttonLayout.setSizeFull();
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        this.add(searchLayout, buttonSearchLayout, meetingGrid, buttonLayout);
    }

    private List<VaadinMeeting> getAllMeetings(List<Meeting> collection) {
        List<VaadinMeeting> vaadinMeetings = new ArrayList<>();
        for (Meeting meeting : collection) {
            VaadinMeeting vaadinMeeting = new VaadinMeeting();
            vaadinMeeting.setId(meeting.getId());
            vaadinMeeting.setTheme(meeting.getTheme());
            vaadinMeeting.setTime(meeting.getTime());
            Division division = divisionService.findDivisionById(meeting.getDivisionOrg());
            vaadinMeeting.setDivisionOrg(division.getName());
            vaadinMeeting.setDivisionOrgId(division.getId());
            Employee employee = employeeService.findEmployeeById(meeting.getResponsible());
            vaadinMeeting.setResponsible(getShortName(employee));
            vaadinMeeting.setResponsibleFullName(employee.getName());
            vaadinMeeting.setResponsibleId(employee.getId());
            vaadinMeeting.setParticipantCount(participantService.getCountParticipantByMeetingId(meeting.getId()));
            vaadinMeetings.add(vaadinMeeting);
        }

        return vaadinMeetings;
    }

    private List<VaadinMeeting> getAllMeetingsBySearchFields(String themeName, String divisionName, LocalDate startDate, LocalDate endDate, String employeeName) {
        int divisionId = 0;
        if (divisionName != null) {
            for (Division division : divisionList) {
                if (divisionName.equals(division.getName())) {
                    divisionId = division.getId();
                }
            }
        }

        int employeeId = 0;
        if (employeeName != null) {
            String searchNameInMap = employeeMap.get(employeeName);
            for (Employee employee : employeeList) {
                if (employee.getName().equals(searchNameInMap)) {
                    employeeId = employee.getId();
                }
            }
        }

        List<Meeting> meetingsAfterSearch = meetingService.findAllMeetingsWith(themeName, divisionId, startDate, endDate, employeeId);
        return this.getAllMeetings(meetingsAfterSearch);
    }

    private void findThemeListener() {
        if (themeSearch.isEmpty() && chooseDivision.isEmpty() && startDate.isEmpty() && endDate.isEmpty() && chooseEmployee.isEmpty()) {
            Notification.show("Search fields are empty").setPosition(Notification.Position.TOP_STRETCH);
        } else {
            meetingGrid.setItems(this.getAllMeetingsBySearchFields(themeSearch.getValue(), chooseDivision.getValue(), startDate.getValue(), endDate.getValue(), chooseEmployee.getValue()));
        }
    }

    private void clearSearchFieldsListener() {
        themeSearch.clear();
        chooseDivision.clear();
        startDate.clear();
        endDate.clear();
        chooseEmployee.clear();
        meetingGrid.setItems(this.getAllMeetings(meetings));
    }

    private String getShortName(Employee employee) {
        String[] arrayName = employee.getName().split(" ");
        String shortName = arrayName[0] + " " + arrayName[1].charAt(0) + ".";
        if (arrayName.length == 3) {
            shortName += arrayName[2].charAt(0) + ".";
        }
        return shortName;
    }

    private void gridClickListener() {
        if (!meetingGrid.getSelectedItems().isEmpty()) {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    private void editButtonListener() {
        Utils.setVaadinMeeting(meetingGrid.getSelectedItems().stream().findFirst().get());
        UI.getCurrent().navigate("/data");
    }

    private void deleteTheme(Notification notification) {
        participantService.deleteByMeetingId(meetingGrid.getSelectedItems().stream().findFirst().get().getId());
        meetingService.deleteById(meetingGrid.getSelectedItems().stream().findFirst().get().getId());
        notification.close();
        refreshGrid();
    }

    private void refreshGrid() {
        meetingGrid.setItems(getAllMeetings(meetingService.findAllMeetings()));
        meetingGrid.getDataProvider().refreshAll();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        Div info = new Div(new Text("Delete theme \"" + meetingGrid.getSelectedItems().stream().findFirst().get().getTheme() + "\"?"));
        Button deleteBtn = new Button("Delete", clickEvent -> deleteTheme(notification));
        deleteBtn.getStyle().set("margin", "0 0 0 var(--lumo-space-l)");
        HorizontalLayout layout = new HorizontalLayout(info, deleteBtn, createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.add(layout);
        return notification;
    }

    public Button createCloseBtn(Notification notification) {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create(), clickEvent -> notification.close());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return closeBtn;
    }
}
