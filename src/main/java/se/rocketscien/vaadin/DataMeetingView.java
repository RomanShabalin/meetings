package se.rocketscien.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import se.rocketscien.entity.Division;
import se.rocketscien.entity.Employee;
import se.rocketscien.entity.Meeting;
import se.rocketscien.entity.Participant;
import se.rocketscien.service.DivisionService;
import se.rocketscien.service.EmployeeService;
import se.rocketscien.service.MeetingService;
import se.rocketscien.service.ParticipantService;
import se.rocketscien.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route("/data")
@PageTitle("Data meetings")
public class DataMeetingView extends VerticalLayout {
    private final DivisionService divisionService;
    private final EmployeeService employeeService;
    private final ParticipantService participantService;
    private final MeetingService meetingService;

    private TextField themeField;
    private DatePicker dateField;
    private ComboBox<Division> divisionComboBox;
    private ComboBox<Employee> responsibleComboBox;

    private HorizontalLayout themeLayout;
    private HorizontalLayout dateLayout;
    private HorizontalLayout divisionLayout;
    private HorizontalLayout responsibleLayout;
    private Grid<VaadinParticipant> participantGrid;
    private CheckboxGroup<Employee> employeeCheckboxGroup;
    private Details employeeDetails;
    private HorizontalLayout buttonLayout;

    private List<Employee> absoluteAllEmployees;
    private List<Employee> allEmployeesByThemeId;

    public DataMeetingView(DivisionService divisionService, EmployeeService employeeService, ParticipantService participantService, MeetingService meetingService) {
        this.divisionService = divisionService;
        this.employeeService = employeeService;
        this.participantService = participantService;
        this.meetingService = meetingService;

        absoluteAllEmployees = this.employeeService.findAllEmployees();

        if (Utils.getVaadinMeeting() != null) {
            allEmployeesByThemeId = this.employeeService.findAllEmployeesByMeetingId(Utils.getVaadinMeeting().getId());
        }

        themeLayout = createThemeLayout();
        dateLayout = createDateLayout();
        divisionLayout = createDivisionLayout();
        responsibleLayout = createResponsibleLayout();
        employeeCheckboxGroup = createEmployeeCheckboxGroup();
        employeeDetails = createEmployeeDetails(employeeCheckboxGroup);
        buttonLayout = createButtonLayout();

        if (Utils.getVaadinMeeting() != null) {
            participantGrid = createGrid();
            this.add(themeLayout, dateLayout, divisionLayout, responsibleLayout, participantGrid, employeeDetails, buttonLayout);
        } else {
            this.add(themeLayout, dateLayout, divisionLayout, responsibleLayout, employeeDetails, buttonLayout);
        }
    }

    private HorizontalLayout createThemeLayout() {
        Label themeLabel = new Label("Theme");
        themeLabel.setWidth("100px");
        themeLabel.add(createRequiredChar());
        themeField = new TextField();
        themeField.setWidth("300px");
        if (Utils.getVaadinMeeting() != null) {
            themeField.setValue(Utils.getVaadinMeeting().getTheme());
        }
        HorizontalLayout themeLayout = new HorizontalLayout(themeLabel, themeField);
        themeLayout.setAlignItems(Alignment.CENTER);
        themeLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return themeLayout;
    }

    private HorizontalLayout createDateLayout() {
        Label dateLabel = new Label("Date");
        dateLabel.setWidth("100px");
        dateLabel.add(createRequiredChar());
        DatePicker.DatePickerI18n dateFieldI18n = new DatePicker.DatePickerI18n();
        dateField = new DatePicker();
        dateFieldI18n.setDateFormat("yyyy-MM-dd");
        dateField.setI18n(dateFieldI18n);
        dateField.setWidth("300px");
        if (Utils.getVaadinMeeting() != null) {
            dateField.setValue(Utils.getVaadinMeeting().getTime());
        }
        HorizontalLayout dateLayout = new HorizontalLayout(dateLabel, dateField);
        dateLayout.setAlignItems(Alignment.CENTER);
        dateLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return dateLayout;
    }

    private HorizontalLayout createDivisionLayout() {
        responsibleComboBox = new ComboBox<>();

        Label divisionLabel = new Label("Division");
        divisionLabel.setWidth("100px");
        divisionLabel.add(createRequiredChar());
        divisionComboBox = new ComboBox<>();
        divisionComboBox.setWidth("300px");
        divisionComboBox.setItems(this.divisionService.findAllDivisions());
        divisionComboBox.setItemLabelGenerator(Division::getName);
        divisionComboBox.addValueChangeListener(listener -> {
            responsibleComboBox.setItems(this.employeeService.findAllEmployeesByDivisionId(divisionComboBox.getValue().getId()));
        });
        if (Utils.getVaadinMeeting() != null) {
            Division division = new Division();
            division.setId(Utils.getVaadinMeeting().getDivisionOrgId());
            division.setName(Utils.getVaadinMeeting().getDivisionOrg());
            divisionComboBox.setValue(division);
        }
        return new HorizontalLayout(divisionLabel, divisionComboBox);
    }

    private HorizontalLayout createResponsibleLayout() {
        Label responsibleLabel = new Label("Responsible");
        responsibleLabel.setWidth("100px");
        responsibleLabel.add(createRequiredChar());
        responsibleComboBox.setWidth("300px");
        responsibleComboBox.setItemLabelGenerator(Employee::getName);
        if (Utils.getVaadinMeeting() != null) {
            Employee employee = new Employee();
            employee.setId(Utils.getVaadinMeeting().getResponsibleId());
            employee.setName(Utils.getVaadinMeeting().getResponsibleFullName());
            employee.setDivisionId(Utils.getVaadinMeeting().getDivisionOrgId());
            responsibleComboBox.setValue(employee);
        }
        return new HorizontalLayout(responsibleLabel, responsibleComboBox);
    }

    private Grid<VaadinParticipant> createGrid() {
        Grid<VaadinParticipant> participantGrid = new Grid<>(VaadinParticipant.class);
        participantGrid.setHeight("300px");
        participantGrid.removeColumnByKey("id");
        participantGrid.setColumnOrder(participantGrid.getColumnByKey("nameParticipant"),
                                       participantGrid.getColumnByKey("years"),
                                       participantGrid.getColumnByKey("division"));
        participantGrid.setItems(this.getAllParticipants());
        participantGrid.setSortableColumns("division");
        participantGrid.setWidthFull();
        return participantGrid;
    }

    private CheckboxGroup<Employee> createEmployeeCheckboxGroup() {
        CheckboxGroup<Employee> employeeCheckboxGroup = new CheckboxGroup<>();
        employeeCheckboxGroup.setItems(absoluteAllEmployees);
        employeeCheckboxGroup.setItemLabelGenerator(Employee::getName);
        employeeCheckboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        if (Utils.getVaadinMeeting() != null) {
            List<Employee> employeesMustBeSelected = new ArrayList<>();
            allEmployeesByThemeId.forEach(employee -> {
                absoluteAllEmployees.forEach(emp -> {
                    if (employee.getId() == emp.getId()) {
                        employeesMustBeSelected.add(emp);
                    }
                });
            });

            employeeCheckboxGroup.select(employeesMustBeSelected);
        }
        return employeeCheckboxGroup;
    }

    private Details createEmployeeDetails(CheckboxGroup<Employee> employeeCheckboxGroup) {
        Details employeeDetails = new Details("Participants:", employeeCheckboxGroup);
        employeeDetails.addThemeVariants(DetailsVariant.FILLED);
        return employeeDetails;
    }

    private HorizontalLayout createButtonLayout() {
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(listener -> saveButtonListener());
        Button closeButton = new Button("Back");
        closeButton.addClickListener(listener -> {
            UI.getCurrent().navigate("meeting");
            Utils.setVaadinMeeting(null);
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, closeButton);
        buttonLayout.setSizeFull();
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return buttonLayout;
    }

    private List<VaadinParticipant> getAllParticipants() {
        List<VaadinParticipant> vaadinParticipants = new ArrayList<>();
        for (Employee employee : allEmployeesByThemeId) {
            VaadinParticipant vaadinParticipant = new VaadinParticipant();
            vaadinParticipant.setId(employee.getId());
            vaadinParticipant.setNameParticipant(employee.getName());
            LocalDate birthDate = employee.getBirthDate();
            LocalDate currentDate = LocalDate.now();
            int years;
            if (birthDate.getMonth().getValue() < currentDate.getMonth().getValue()) {
                years = currentDate.getYear() - birthDate.getYear();
            } else if (birthDate.getMonth().getValue() > currentDate.getMonth().getValue()) {
                years = currentDate.getYear() - birthDate.getYear() - 1;
            } else {
                if (birthDate.getDayOfMonth() <= currentDate.getDayOfMonth()) {
                    years = currentDate.getYear() - birthDate.getYear();
                } else {
                    years = currentDate.getYear() - birthDate.getYear() - 1;
                }
            }
            vaadinParticipant.setYears(years);
            vaadinParticipant.setDivision(this.divisionService.findDivisionById(employee.getDivisionId()).getName());
            vaadinParticipants.add(vaadinParticipant);
        }

        return vaadinParticipants;
    }

    private void saveButtonListener() {
        if (themeField.getValue().isEmpty() || themeField.getValue() == null ||
                dateField.isEmpty() || dateField.getValue() == null ||
                divisionComboBox.isEmpty() || divisionComboBox.getValue() == null ||
                responsibleComboBox.isEmpty() || responsibleComboBox.getValue() == null) {
            Notification.show("Fill fields").setPosition(Notification.Position.TOP_STRETCH);
        } else {
            if (Utils.getVaadinMeeting() != null) {
                participantService.deleteByMeetingId(Utils.getVaadinMeeting().getId());
                saveOrUpdateTheme(Utils.getVaadinMeeting().getId());
            } else {
                int newMeetingId = meetingService.getLastId() + 1;
                saveOrUpdateTheme(newMeetingId);
            }
            UI.getCurrent().navigate("/meeting");
        }
        Utils.setVaadinMeeting(null);
    }

    private void saveOrUpdateTheme(int meetingId) {
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTheme(themeField.getValue());
        meeting.setTime(dateField.getValue());
        meeting.setDivisionOrg(divisionComboBox.getValue().getId());
        meeting.setResponsible(responsibleComboBox.getValue().getId());
        meetingService.save(meeting);

        employeeCheckboxGroup.getSelectedItems().forEach(employee -> {
            Participant participant = new Participant();
            participant.setMeetingId(meetingId);
            participant.setEmployeeId(employee.getId());
            participantService.save(participant);
        });
    }

    private Label createRequiredChar() {
        Label requiredChar = new Label("*");
        requiredChar.getStyle().set("color", "red");
        return requiredChar;
    }
}
