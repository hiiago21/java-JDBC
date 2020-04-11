package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listener.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationExcepetion;
import model.services.DepartmentService;
import model.services.SellerServices;

public class SellerFormController implements Initializable {

	private Seller seller;
	private SellerServices servSeller;
	private DepartmentService dpService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private ObservableList<Department> obsList;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDep;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		try {
			seller = getFormData();
			servSeller.saverOrUpadete(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationExcepetion e) {
			setErrorMessages(e.getErrors());
		}

	}

	// manda uma notificação de mudança nos objetos
	private void notifyDataChangeListeners() {
		for (DataChangeListener listeners : dataChangeListeners) {
			listeners.onDataChanged();
		}

	}

	private Seller getFormData() {
		ValidationExcepetion validation = new ValidationExcepetion("Validation error!");

		Seller seller = new Seller();
		
		seller.setId(Utils.tryParseToInt(txtID.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			validation.setErrors("nome", "Campo não pode ser vazio");
		}
		seller.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			validation.setErrors("email", "Campo não pode ser vazio");
		}
		seller.setEmail(txtEmail.getText());
		
		if(dpBirthDate.getValue() == null) {
			validation.setErrors("birthDate", "Campo não pode ser vazio");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			seller.setBirthDate(Date.from(instant));
		}
		
		if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			validation.setErrors("baseSalary", "Campo não pode ser vazio");
		}
		seller.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

		if (validation.getErrors().size() > 0) {
			throw validation;
		}
		
		seller.setDepartment(comboBoxDep.getValue());

		return seller;
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
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();

	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void setSellerService(SellerServices servSeller) {
		this.servSeller = servSeller;
	}

	public void setDepartmentService(DepartmentService dpService) {
		this.dpService = dpService;
	}

	// cria uma lista de objetos que querem saber das mudanças de update ou adição
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void updateFormaData() {
		if (servSeller == null) {
			throw new IllegalStateException("Vazio");
		}
		txtID.setText(String.valueOf(seller.getId()));
		txtName.setText(seller.getName());
		txtEmail.setText(seller.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if (seller.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if(seller.getDepartment() == null) {
			comboBoxDep.getSelectionModel().selectFirst();
		}
		comboBoxDep.setValue(seller.getDepartment());
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> filds = errors.keySet();

		labelErrorName.setText((filds.contains("nome") ? errors.get("nome") : " "));		
		labelErrorEmail.setText((filds.contains("email") ? errors.get("email") : " "));
		labelErrorBaseSalary.setText((filds.contains("baseSalary") ? errors.get("baseSalary") : " "));
		labelErrorBirthDate.setText((filds.contains("birthDate") ? errors.get("birthDate") : " "));
	}

	public void loadAssociateObjects() {
		List<Department> list = dpService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDep.setItems(obsList);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDep.setCellFactory(factory);
		comboBoxDep.setButtonCell(factory.call(null));
	}

}
