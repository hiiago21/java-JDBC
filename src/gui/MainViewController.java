package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedores;
	@FXML
	private MenuItem menuItemDepartamentos;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemVendedoresAction() {
		System.out.println("onMenuItemVendedoresAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@FXML
	public void onMenuItemDepartamentosAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) ->{
		controller.setDepService(new DepartmentService());
		controller.updateTableView();
		});
		//passando uma funcao lambda para  injetar dependencia em DepControler de Service
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	
	//metodo recebendo uma expressão lambda para tornar generico
	private synchronized <T> void loadView(String absoluteName, Consumer<T> actionIni) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVbox = loader.load();
		
		//salva uma referencia pra view principal
		Scene mainScene = Main.getMainScene();
		
		//cria uma referencia pro conteudo da view principal
		VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
		
		//salva os filhos da view principal e limpa a view principal
		Node mainMenu = mainVbox.getChildren().get(0);
		mainVbox.getChildren().clear();
		
		//adiciona os filhos das duasd view
		mainVbox.getChildren().add(mainMenu);
		mainVbox.getChildren().addAll(newVbox.getChildren());
		
		// ACIONA O CONSUMER
		T controller = loader.getController();
		actionIni.accept(controller);
		}
		catch(IOException e){
			Alerts.showAlert("IO Exception", null , e.getMessage(), AlertType.ERROR);
		}
	}

}
