package QuickCustomerManagment;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

	@FXML
	private ComboBox defaultcurrencySettingsCombobox;
	@FXML
	private TextField companyDetailsCompanynameField = new TextField();
	@FXML
	private TextField companyDetailsCompanystreetField = new TextField();
	@FXML
	private TextField companyDetailsCompanypostcodeField = new TextField();
	@FXML
	private TextField companyDetailsCompanycityField = new TextField();
	@FXML
	private TextField companyDetailsCompanycountyField = new TextField();
	@FXML
	private TextField companyDetailsCompanycountryField = new TextField();
	@FXML
	private TextField companyDetailsCompanytaxnoField = new TextField();
	@FXML
	private TextField companyDetailsCompanyemailField = new TextField();
	@FXML
	private TextField companyDetailsCompanytelephoneField = new TextField();
	@FXML
	private TextField companyDetailsCompanywebsiteField = new TextField();
	@FXML
	private TextArea companyDetailsCompanyadditionalinfoTextarea = new TextArea();

	@FXML
	private TextArea noteSettingsTextarea = new TextArea();
	private AppDataSettings settings = new AppDataSettings();
	public static Stage SETTINGSWINDOW;

	@FXML
	private Text settingsHeader = new Text();
	@FXML
	private Text defaultcurrencySettingsDesc = new Text();
	@FXML
	private Text companylogoSettingsDesc = new Text();
	@FXML
	private TextField companylogopath = new TextField();
	@FXML
	private Button opencompanylogoButton = new Button();
	@FXML
	private Label yourCompanyDetailsLabel = new Label();
	@FXML
	private Text companyDetailsCompanynameDesc = new Text();
	@FXML
	private Text companyDetailsCompanystreetDesc = new Text();
	@FXML
	private Text companyDetailsCompanypostcodeDesc = new Text();
	@FXML
	private Text companyDetailsCompanycityDesc = new Text();
	@FXML
	private Text companyDetailsCompanycountyDesc = new Text();
	@FXML
	private Text companyDetailsCompanycountryDesc = new Text();
	@FXML
	private Text companyDetailsCompanytaxnoDesc = new Text();
	@FXML
	private Text companyDetailsCompanyemailDesc = new Text();
	@FXML
	private Text companyDetailsCompanytelephoneDesc = new Text();
	@FXML
	private Text companyDetailsCompanywebsiteDesc = new Text();
	@FXML
	private Text companyDetailsCompanyadditionalinfoDesc = new Text();
	@FXML
	private Text noteSettingsDesc = new Text();
	@FXML
	private Button savesettingsButton = new Button();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// load settings to display in settings window
		this.settings.loadAppSettings();
		this.settings.loadCurrenciesCountries();
		for (String currency : this.settings.getCurrencies().keySet()) {
			if (!currency.equals("")) {
				this.defaultcurrencySettingsCombobox.getItems().add(currency);
			}
		}
		this.defaultcurrencySettingsCombobox.getSelectionModel()
				.select(settings.getAppsettings().get("defaultCurrencyIso"));
		this.companylogopath.setText(settings.getAppsettings().get("invoiceCompanylogopath"));
		this.companyDetailsCompanynameField.setText(settings.getAppsettings().get("invoiceCompanyname"));
		this.companyDetailsCompanystreetField.setText(settings.getAppsettings().get("invoiceCompanystreet"));
		this.companyDetailsCompanypostcodeField.setText(settings.getAppsettings().get("invoiceCompanypostcode"));
		this.companyDetailsCompanycityField.setText(settings.getAppsettings().get("invoiceCompanycity"));
		this.companyDetailsCompanycountyField.setText(settings.getAppsettings().get("invoiceCompanycounty"));
		this.companyDetailsCompanycountryField.setText(settings.getAppsettings().get("invoiceCompanycountry"));
		this.companyDetailsCompanytaxnoField.setText(settings.getAppsettings().get("invoiceCompanytaxno"));
		this.companyDetailsCompanyemailField.setText(settings.getAppsettings().get("invoiceCompanyemail"));
		this.companyDetailsCompanytelephoneField.setText(settings.getAppsettings().get("invoiceCompanytelephone"));
		this.companyDetailsCompanywebsiteField.setText(settings.getAppsettings().get("invoiceCompanywebsite"));
		this.companyDetailsCompanyadditionalinfoTextarea
				.setText(settings.getAppsettings().get("invoiceCompanyadditionalinfo"));
		this.noteSettingsTextarea.setText(settings.getAppsettings().get("settingsNote"));
		loadSettingsWindowText();
		this.companylogopath.setEditable(false);
	}

	private void loadSettingsWindowText() {
		if (settings.getAppsettings().get("invoiceCompanylogopath") == null
				|| settings.getAppsettings().get("invoiceCompanylogopath").equals("")
				|| settings.getAppsettings().get("invoiceCompanylogopath").equals(" ")) {
			this.companylogopath.setText(" * Max. 200 x 100 Pixel * ");
		}
		this.settingsHeader.setText(AppDataSettings.languageBundle.getString("settingsWindowHeaderText"));
		this.defaultcurrencySettingsDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowDefaultcurrencyText"));
		this.settingsHeader.setText(AppDataSettings.languageBundle.getString("settingsWindowDefaultcurrencyText"));
		this.companylogoSettingsDesc.setText(AppDataSettings.languageBundle.getString("settingsWindowCompanylogoText"));
		this.opencompanylogoButton.setText(AppDataSettings.languageBundle.getString("settingsWindowOpenButtonText"));
		this.yourCompanyDetailsLabel
				.setText(AppDataSettings.languageBundle.getString("settingsWindowCompanydetailshintText"));
		this.companyDetailsCompanynameDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowCompanynameText"));
		this.companyDetailsCompanystreetDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowCompanystreetText"));
		this.companyDetailsCompanypostcodeDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowPostcodeText"));
		this.companyDetailsCompanycityDesc.setText(AppDataSettings.languageBundle.getString("settingsWindowCityText"));
		this.companyDetailsCompanycountyDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowCountyText"));
		this.companyDetailsCompanycountryDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowCountryText"));
		this.companyDetailsCompanytaxnoDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowTaxnoText"));
		this.companyDetailsCompanyemailDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowEmailText"));
		this.companyDetailsCompanytelephoneDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowPhoneText"));
		this.companyDetailsCompanywebsiteDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowWebsiteText"));
		this.companyDetailsCompanyadditionalinfoDesc
				.setText(AppDataSettings.languageBundle.getString("settingsWindowAdditionalinfoText"));
		this.noteSettingsDesc.setText(AppDataSettings.languageBundle.getString("settingsWindowNoteText"));
		this.savesettingsButton.setText(AppDataSettings.languageBundle.getString("settingsWindowSaveButtonText"));
	}

	@FXML
	public void handleSaveSettings() {
		settings.updateSetting("defaultCurrencyIso",
				this.defaultcurrencySettingsCombobox.getSelectionModel().getSelectedItem().toString());
		settings.updateSetting("settingsNote", this.noteSettingsTextarea.getText());
		if (this.companylogopath.getText().contains(".")) {
			settings.updateSetting("invoiceCompanylogopath", this.companylogopath.getText());
		}
		settings.updateSetting("invoiceCompanyname", this.companyDetailsCompanynameField.getText());
		settings.updateSetting("invoiceCompanystreet", this.companyDetailsCompanystreetField.getText());
		settings.updateSetting("invoiceCompanypostcode", this.companyDetailsCompanypostcodeField.getText());
		settings.updateSetting("invoiceCompanycity", this.companyDetailsCompanycityField.getText());
		settings.updateSetting("invoiceCompanycounty", this.companyDetailsCompanycountyField.getText());
		settings.updateSetting("invoiceCompanycountry", this.companyDetailsCompanycountryField.getText());
		settings.updateSetting("invoiceCompanytaxno", this.companyDetailsCompanytaxnoField.getText());
		settings.updateSetting("invoiceCompanyemail", this.companyDetailsCompanyemailField.getText());
		settings.updateSetting("invoiceCompanytelephone", this.companyDetailsCompanytelephoneField.getText());
		settings.updateSetting("invoiceCompanywebsite", this.companyDetailsCompanywebsiteField.getText());
		settings.updateSetting("invoiceCompanyadditionalinfo",
				this.companyDetailsCompanyadditionalinfoTextarea.getText());

		SETTINGSWINDOW.close();
	}

	@FXML
	public void handleOpenCompanyLogo() {
		FileChooser companylogoChooser = new FileChooser();
		companylogoChooser.setTitle(AppDataSettings.languageBundle.getString("settingsWindowOpenWindowTitleText"));
		companylogoChooser.getExtensionFilters().add(new ExtensionFilter("PNG Image", "*.png"));
		companylogoChooser.getExtensionFilters().add(new ExtensionFilter("JPEG Image", "*.jpeg"));
		companylogoChooser.getExtensionFilters().add(new ExtensionFilter("JPG Image", "*.jpg"));
		companylogoChooser.setSelectedExtensionFilter(new ExtensionFilter("PNG Image", "*.png"));

		try {
			File companylogofile = companylogoChooser.showOpenDialog((Stage) SETTINGSWINDOW.getScene().getWindow());
			this.companylogopath.setText(companylogofile.getAbsolutePath());
		} catch (NullPointerException e) {
			System.out.println("" + e);
		}
	}

	public ComboBox getDefaultcurrencySettingsCombobox() {
		return defaultcurrencySettingsCombobox;
	}

	public void setDefaultcurrencySettingsCombobox(ComboBox defaultcurrencySettingsCombobox) {
		this.defaultcurrencySettingsCombobox = defaultcurrencySettingsCombobox;
	}

	public TextArea getNoteSettingsTextarea() {
		return noteSettingsTextarea;
	}

	public void setNoteSettingsTextarea(TextArea noteSettingsTextarea) {
		this.noteSettingsTextarea = noteSettingsTextarea;
	}

}
