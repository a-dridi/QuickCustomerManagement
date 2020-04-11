package QuickCustomerManagment;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.InvoicePos;

public class AddProductsController implements Initializable {

	@FXML
	private Text addProductsHeader = new Text();
	@FXML
	private Text productnameAddProductsDesc = new Text();
	@FXML
	private Text pricerperunitAddProductsDesc = new Text();
	@FXML
	private Text availableamountAddProductsDesc = new Text();
	@FXML
	private Text noteAddProductsDesc = new Text();

	@FXML
	private TextField productnameAddProductsField;
	@FXML
	private TextField pricerperunitAddProductsField;
	@FXML
	private TextField availableamountAddProductsField;
	@FXML
	private TextField noteAddProductsField;
	@FXML
	private Button AddAddProductsButton = new Button();
	@FXML
	private Text windowInfoAddProductsText = new Text();
	
	private AppDataSettings loadData;
	public static Stage addProductsWindow;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadData = new AppDataSettings();
		this.availableamountAddProductsField.setText("-1");
		loadAddProductsWindowText();

	}

	private void loadAddProductsWindowText() {
		this.addProductsHeader.setText(AppDataSettings.languageBundle.getString("addProductsWindowHeaderTxt"));
		this.productnameAddProductsDesc
				.setText(AppDataSettings.languageBundle.getString("addProductsWindowProductnameTxt"));
		this.pricerperunitAddProductsDesc
				.setText(AppDataSettings.languageBundle.getString("addProductsWindowPriceperunitTxt"));
		this.availableamountAddProductsDesc
				.setText(AppDataSettings.languageBundle.getString("addProductsWindowAvailableAmountTxt"));
		this.noteAddProductsDesc.setText(AppDataSettings.languageBundle.getString("addProductsWindowNoteTxt"));
		this.AddAddProductsButton.setText(AppDataSettings.languageBundle.getString("addProductsWindowAddButtonTxt"));
		this.windowInfoAddProductsText.setText(AppDataSettings.languageBundle.getString("addProductsWindowWindowInfoTxt"));
	}

	@FXML
	public void handleSaveAddProducts(ActionEvent event) {
		double priceperunit;
		Integer availableamount;
		try {
			priceperunit = Double.parseDouble((this.pricerperunitAddProductsField.getText().replace(',', '.')));
			availableamount = Integer.valueOf(this.availableamountAddProductsField.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorAddProductsNoNumber"));
			alert.show();
			return;
		}
		
		if (loadData.addNewProducts(this.productnameAddProductsField.getText(), priceperunit, availableamount, this.noteAddProductsField.getText())) {
			this.addProductsWindow.close();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(AppDataSettings.languageBundle.getString("okWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("okWindowProductsAdded"));
			alert.show();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(loadData.getErrorMessage());
			alert.show();
		}
	}
	
}
