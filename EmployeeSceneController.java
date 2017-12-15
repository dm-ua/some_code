package sample.employeeScene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.employeeScene.dialogEmployeeWindow.Employee;
import sample.employeeScene.dialogEmployeeWindow.EmployeeEditDialogController;
import sample.employeeScene.employeeDB.EmployeeDB;
import sample.machinScene.MachinScene;
import sample.repairScene.RepairScene;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Макс on 29.08.2017.
 */
public class EmployeeSceneController {
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private LocalDate localDate;
    private EmployeeScene employeeScene;
    EmployeeDB repository;

    public EmployeeScene getEmployeeScene(){return employeeScene;}

    public void setEmployeeScene(EmployeeScene employeeScene){
        this.employeeScene = employeeScene;
        employeeTable.setItems(repository.getEmployeeRepositoryList());
    }


    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> firstNameOfEmployeeColumn;
    @FXML
    private TableColumn<Employee, String> secondNameOfEmployeeColumn;
    @FXML
    private TableColumn<Employee, String> dateOfBirthdayColumn;
    @FXML
    private TableColumn<Employee, String> passportColumn;
    @FXML
    private TableColumn<Employee, Long> idNumberColumn;
    @FXML
    private DatePicker datePicker;

    public EmployeeSceneController(){repository = new EmployeeDB();}

    @FXML
    public void handleOnFromDate() {
        this.localDate = datePicker.getValue();
        System.out.println(this.localDate);
    }

    @FXML
    private void handleDeleteEmployee() {
        int selectedIndex = employeeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            employeeTable.getItems().remove(selectedIndex);
            Employee employee = employeeTable.getSelectionModel().getSelectedItem();
            String firstNameOfEmployee = employee.getFirstNameOfEmployee();
            String secondNameOfEmployee = employee.getSecondNameOfEmployee();
            repository.remove(firstNameOfEmployee, secondNameOfEmployee);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Помилка");
            alert.setHeaderText("Не вибраний працівник");
            alert.setContentText("Будь ласка виберіть працівника, для того щоб видалити.");
            alert.showAndWait();
        }
    }
    public  boolean showEmployeeEditDialog(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EmployeeEditDialogController.class.getResource("EmployeeEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редагування працівників");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            EmployeeEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEmployee(employee);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleNewEmployee() {
        Employee tempEmployee = new Employee();
        boolean okClicked = showEmployeeEditDialog(tempEmployee);
        if (okClicked) {
            employeeTable.getItems().add(tempEmployee);
            repository.insertIntoDB(tempEmployee);
        }
    }

    @FXML
    private void handleEditEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            boolean okClicked = showEmployeeEditDialog(selectedEmployee);
            if (okClicked) {
                int selectedIndex =
                        employeeTable.getSelectionModel().getSelectedIndex();
                employeeData.set(selectedIndex, selectedEmployee);
                repository.updateIntoDB(
                        selectedEmployee.getFirstNameOfEmployee(),
                        selectedEmployee.getSecondNameOfEmployee(),
                        selectedEmployee.getDateOfBirthday(),
                        selectedEmployee.getPassport(),
                        selectedEmployee.getIdNumber());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Помилка");
            alert.setHeaderText("Не вибрано працівника");
            alert.setContentText("Будь ласка, виберіть працівника з таблички");
            alert.showAndWait();
        }
    }

    @FXML
    public void goToMachinScene(ActionEvent event) {
        try {
            new MachinScene();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void goToRepairScene(ActionEvent event) {
        try {
            new RepairScene();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToJobScene(ActionEvent event) {

        try {
            new Main();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        firstNameOfEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("firstNameOfEmployee"));
        secondNameOfEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("secondNameOfEmployee"));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirthday"));
        passportColumn.setCellValueFactory(new PropertyValueFactory<>("passport"));
        idNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        employeeTable.setItems(employeeData);
    }

}
