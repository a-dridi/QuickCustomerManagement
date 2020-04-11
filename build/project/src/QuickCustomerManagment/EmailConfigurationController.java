package QuickCustomerManagment;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmailConfigurationController implements Initializable {

	private AppDataSettings settings = new AppDataSettings();
	public static Stage EMAILCONFIGURATIONWINDOW;

	@FXML
	private Text emailsettingsHeader = new Text();
	@FXML
	private Text forenamesurnameEmailConfigurationDesc = new Text();
	@FXML
	private Text emailEmailConfigurationDesc = new Text();
	@FXML
	private Text serverEmailConfigurationDesc = new Text();
	@FXML
	private Text portEmailConfigurationDesc = new Text();
	@FXML
	private Text usernameEmailConfigurationDesc = new Text();
	@FXML
	private Text passwordEmailConfigurationDesc = new Text();
	@FXML
	private Text passwordConfirmationEmailConfigurationDesc = new Text();
	
	@FXML
	private TextField forenamesurnameEmailConfigurationValue = new TextField();
	@FXML
	private TextField emailEmailConfigurationValue = new TextField();
	@FXML
	private TextField serverEmailConfigurationValue = new TextField();
	@FXML
	private TextField portEmailConfigurationValue = new TextField();
	@FXML
	private TextField usernameEmailConfigurationValue = new TextField();
	@FXML
	private PasswordField passwordEmailConfigurationValue = new PasswordField();
	@FXML
	private PasswordField passwordConfirmationEmailConfigurationValue = new PasswordField();

	@FXML
	private Button saveEmailConfigurationButton = new Button();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.settings.loadAppSettings();
		this.forenamesurnameEmailConfigurationValue.setText(settings.getAppsettings().get("emailForenameSurname"));
		this.emailEmailConfigurationValue.setText(settings.getAppsettings().get("emailEmailaddress"));
		this.serverEmailConfigurationValue.setText(settings.getAppsettings().get("emailHost"));
		this.portEmailConfigurationValue.setText(settings.getAppsettings().get("emailPort"));
		this.usernameEmailConfigurationValue.setText(settings.getAppsettings().get("emailUsername"));
		this.passwordEmailConfigurationValue.setText(settings.getAppsettings().get("emailPassword"));
		this.passwordConfirmationEmailConfigurationValue.setText(settings.getAppsettings().get("emailPassword"));
		loadEmailConfigurationWindowText();
	}
	
	private void loadEmailConfigurationWindowText() {
		this.emailsettingsHeader.setText(AppDataSettings.languageBundle.getString("emailConfigurationWindowHeaderText"));
		this.forenamesurnameEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowForenameSurnameText"));
		this.emailEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowEmailaddressText"));
		this.serverEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowMailserverText"));
		this.portEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowMailportText"));
		this.usernameEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowUsernameText"));
		this.passwordEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowPasswordText"));
		this.passwordConfirmationEmailConfigurationDesc.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowConfirmPasswordText"));
		this.saveEmailConfigurationButton.setText(AppDataSettings.languageBundle.getString("emailconfigurationWindowSaveButtonText"));
	}

	@FXML
	public void handleSaveSettings() {
		
		if(!this.passwordEmailConfigurationValue.getText().equals(this.passwordConfirmationEmailConfigurationValue.getText())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorPasswordPasswordconfirmationNotSameTxt"));
			alert.show();
			return;
		}
		
		settings.updateSetting("emailForenameSurname", this.forenamesurnameEmailConfigurationValue.getText());
		settings.updateSetting("emailEmailaddress", this.emailEmailConfigurationValue.getText().trim());
		settings.updateSetting("emailHost", this.serverEmailConfigurationValue.getText().trim());
		settings.updateSetting("emailPort", this.portEmailConfigurationValue.getText().trim());
		settings.updateSetting("emailUsername", this.usernameEmailConfigurationValue.getText().trim());
		settings.updateSetting("emailPassword", this.passwordEmailConfigurationValue.getText().trim());

		EMAILCONFIGURATIONWINDOW.close();
	}

	
}
