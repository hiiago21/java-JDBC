package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listener.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationExcepetion;
import model.services.DepartmentService;


public class DepartmentFormController implements Initializable {

	private Department entDep;
	private DepartmentService servDep;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtID;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML 
	public void onBtSalvarAction(ActionEvent event) {
		try{
			entDep = getFormData();
			servDep.saverOrUpadete(entDep);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationExcepetion e) {
			setErrorMessages(e.getErrors());
		}
		
	}
	
	//manda uma notificação de mudança nos objetos 
	private void notifyDataChangeListeners() {
		for (DataChangeListener listeners : dataChangeListeners) {
			listeners.onDataChanged();
		}
		
	}

	private Department getFormData() {
		ValidationExcepetion validation = new ValidationExcepetion("Validation error!");
		
		Department dep = new Department();
		dep.setId(Utils.tryParseToInt(txtID.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			validation.setErrors("vazio", "Campo não pode ser vazio");
		}
		dep.setName(txtName.getText());
		
		if(validation.getErrors().size() > 0) {
			throw validation;
		}
		
		return dep;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
	}

	private void initializeNode() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextFieldMaxLength(txtName, 50);
		
	}
	
	public void setDepartment(Department dep) {
		this.entDep = dep;
	}
	
	public void setDepartmentService(DepartmentService servDep) {
		this.servDep = servDep;
	}
	
	// cria uma lista de objetos que querem saber das mudanças de update ou adição
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void updateFormaData() {
		if (entDep == null) {
			throw new IllegalStateException("Vazio");
		}
		txtID.setText(String.valueOf(entDep.getId()));
		txtName.setText(entDep.getName());
	}
	
	private void setErrorMessages(Map <String, String> errors) {
		Set<String> filds = errors.keySet();
		
		if(filds.contains("vazio")) {
			labelErrorName.setText(errors.get("vazio"));
		}
				
	}

}
