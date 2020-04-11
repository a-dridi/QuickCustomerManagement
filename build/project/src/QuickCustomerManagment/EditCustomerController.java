package QuickCustomerManagment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;

/**
 * Edit Customer Window - FXML Controller class
 *
 * @author
 */

public class EditCustomerController implements Initializable {

	@FXML
	private Text editcustomerHeader = new Text();
	@FXML
	private Text emailclientdesc = new Text();
	@FXML
	private Text forenameclientdesc = new Text();
	@FXML
	private Text surnameclientdesc = new Text();
	@FXML
	private Text companynameclientdesc = new Text();
	@FXML
	private Text taxnumberclientdesc = new Text();
	@FXML
	private Text streetclientdesc = new Text();
	@FXML
	private Text zipcodeclientdesc = new Text();
	@FXML
	private Text cityclientdesc = new Text();
	@FXML
	private Text countyclientdesc = new Text();
	@FXML
	private Text countryclientdesc = new Text();
	@FXML
	private Text currencyclientdesc = new Text();
	@FXML
	private Text noteclientdesc = new Text();

	@FXML
	private ComboBox countryclientcombobox;
	@FXML
	private ComboBox currencyclientcombobox;
	@FXML
	private TextField emailclientfield;
	@FXML
	private TextField forenameclientfield;
	@FXML
	private TextField surnameclientfield;
	@FXML
	private TextField companynameclientfield;
	@FXML
	private TextField taxnumberclientfield;
	@FXML
	private TextField streetclientfield;
	@FXML
	private TextField zipcodeclientfield = new TextField();
	@FXML
	private TextField cityclientfield;
	@FXML
	private TextField countyclientfield;
	@FXML
	private TextArea noteclienttextarea;
	@FXML
	private Button clientsavebutton = new Button();

	private AppDataSettings loadData;
	public static Stage editCustomerWindow;
	public static Integer customerId;
	private Customer editedCustomer;

	/**
	 * Load data (countries, etc.) for the new customer window. Initializes the
	 * controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadData = new AppDataSettings();
		loadData.loadCurrenciesCountries();
		loadData.loadAppSettings();

		this.countryclientcombobox.getItems().addAll(loadData.getCountries());
		for (String currency : loadData.getCurrencies().keySet()) {
			this.currencyclientcombobox.getItems().add(currency);
		}

		this.currencyclientcombobox.getSelectionModel().select(loadData.getAppsettings().get("defaultCurrencyIso"));
		loadEditCustomerWindowText();
		this.editedCustomer = loadData.getCustomerById(customerId);
		loadFieldValues();
	}

	private void loadEditCustomerWindowText() {
		this.editcustomerHeader.setText(AppDataSettings.languageBundle.getString("editcustomerWindowHeaderText"));
		this.emailclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowEmailText"));
		this.forenameclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowForenameText"));
		this.surnameclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowSurnameText"));
		this.companynameclientdesc
				.setText(AppDataSettings.languageBundle.getString("newcustomerWindowCompanynameText"));
		this.taxnumberclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowTaxnoText"));
		this.streetclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowStreetText"));
		this.zipcodeclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowZipcodeText"));
		this.cityclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowCityText"));
		this.countyclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowCountyText"));
		this.countryclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowCountryText"));
		this.currencyclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowCurrencyText"));
		this.noteclientdesc.setText(AppDataSettings.languageBundle.getString("newcustomerWindowNoteText"));
		this.clientsavebutton.setText(AppDataSettings.languageBundle.getString("newcustomerWindowSaveButtonText"));
	}

	private void loadFieldValues() {
		this.countryclientcombobox.getSelectionModel().select(this.editedCustomer.getCountry());
		this.currencyclientcombobox.getSelectionModel().select(this.editedCustomer.getCurrencyiso());
		this.emailclientfield.setText(this.editedCustomer.getEmail());
		this.forenameclientfield.setText(this.editedCustomer.getForename());
		this.surnameclientfield.setText(this.editedCustomer.getLastname());
		this.companynameclientfield.setText(this.editedCustomer.getCompanyname());
		this.taxnumberclientfield.setText(this.editedCustomer.getTaxnumber());
		this.streetclientfield.setText(this.editedCustomer.getStreet());
		this.zipcodeclientfield.setText(""+this.editedCustomer.getZipcode());
		this.cityclientfield.setText(this.editedCustomer.getCity());
		this.countyclientfield.setText(this.editedCustomer.getCounty());
		this.noteclienttextarea.setText(this.editedCustomer.getNote());
	}

	@FXML
	private void handleSaveCustomerAction(ActionEvent event) {

		try {
			Integer zipCodeNumber = Integer.parseInt(zipcodeclientfield.getText());
			if (loadData.updateCustomer(emailclientfield.getText(), forenameclientfield.getText(),
					surnameclientfield.getText(), companynameclientfield.getText(), taxnumberclientfield.getText(),
					streetclientfield.getText(), zipCodeNumber, cityclientfield.getText(), countyclientfield.getText(),
					countryclientcombobox.getValue().toString(), currencyclientcombobox.getValue().toString(),
					noteclienttextarea.getText())) {
				this.editCustomerWindow.close();

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle(AppDataSettings.languageBundle.getString("okWindowHeader").toUpperCase());
				alert.setContentText(AppDataSettings.languageBundle.getString("okWindowCustomerUpdated"));
				alert.show();
				
				ViewCustomersController.reloadCustomers();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
				alert.setContentText(loadData.getErrorMessage());
				alert.show();
			}

		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorZipcode"));
			alert.show();
		}

	}

	public AppDataSettings getLoadData() {
		return loadData;
	}

	public void setLoadData(AppDataSettings loadData) {
		this.loadData = loadData;
	}

}
