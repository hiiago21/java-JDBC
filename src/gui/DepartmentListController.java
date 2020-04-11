package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService serviceDep;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColunmId;
	
	@FXML
	private TableColumn<Department, String> tableColunmName;
	
	@FXML
	private Button buttonNovo;
	
	private ObservableList<Department> obsDep;
	
	@FXML
	public void onButtonAction() {
		System.out.println("Ok");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		
	}
	

	private void initializeNode() {
		tableColunmId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunmName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void setDepService(DepartmentService serviceDep) {
		this.serviceDep = serviceDep;
	}
	
	public void updateTableView() {
		
		if(serviceDep == null) {
			throw new IllegalStateException("ServiceDep está vazio");
		}
		List<Department> list = serviceDep.findAll();
		
		obsDep = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsDep);
	}

}
