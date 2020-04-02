package QuickCustomerManagment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Desktop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.UnpaidInvoice;;

public class MainWindowController implements Initializable {

	@FXML
	private Text applicationHeaderTitle = new Text();
	@FXML
	private Text unpaidInvoicesLabel = new Text();
	@FXML
	private Button addnewinvoiceButton = new Button();
	@FXML
	private Button addnewcustomerButton = new Button();
	@FXML
	private Button showInvoicesFolderButton = new Button();
	@FXML
	private Button settingsButton = new Button();
	@FXML
	private Button emailConfigurationButton = new Button();

	@FXML
	private ComboBox languageSelector = new ComboBox();;

	@FXML
	private ImageView addIcon = new ImageView();
	@FXML
	private ImageView eyeIcon = new ImageView();

	@FXML
	private TableView unpaidInvoices = new TableView();

	private List<UnpaidInvoice> unpaidInvoicesList = new ArrayList<>();
	public static ObservableList<UnpaidInvoice> unpaidInvoicesData = FXCollections.observableArrayList();

	private AppDataSettings settings = new AppDataSettings();

	private TableColumn unpaidInvoiceDate;
	private TableColumn unpaidInvoiceSum;
	private TableColumn unpaidInvoiceCurrencyIso;
	private TableColumn unpaidInvoiceCustomercompanyname;
	private TableColumn unpaidInvoiceCustomerforename;
	private TableColumn unpaidInvoiceCustomersurname;
	private TableColumn unpaidInvoiceCustomerpaybutton;
	private TableColumn unpaidInvoiceCustomershowinvoicebutton;

	// Languages List
	public static Map<String, String> langList = new TreeMap<String, String>() {
		{
			put("en", "English");
			put("de", "Deutsch");
		}
	};

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// Load language
		for (String lang : langList.values()) {
			this.languageSelector.getItems().add(lang);
		}
		// Check if user's default system language is available. If not, then use
		// English as language
		if (langList.containsKey(Locale.getDefault().getLanguage())) {
			this.settings.loadUIText(Locale.getDefault().getLanguage());
			this.languageSelector.getSelectionModel().select(langList.get(Locale.getDefault().getLanguage()));
		} else {
			this.settings.loadUIText("en");
			this.languageSelector.getSelectionModel().select(langList.get("en"));
		}
		settings.loadDatabase();
		// settings.loadAppSettings();
		loadUnpaidInvoices();
		loadUiMainWindowsText();
	}

	private void loadUiMainWindowsText() {
		this.applicationHeaderTitle.setText(AppDataSettings.languageBundle.getString("mainWindowsApplicationTitleText"));
		this.unpaidInvoicesLabel.setText(AppDataSettings.languageBundle.getString("mainWindowsUnpaidinvoicesText"));
		this.addnewinvoiceButton
				.setText(AppDataSettings.languageBundle.getString("mainWindowsaddnewinvoicebuttonText"));
		this.addnewcustomerButton
				.setText(AppDataSettings.languageBundle.getString("mainWindowsaddnewcustomerbuttonText"));
		this.showInvoicesFolderButton
				.setText(AppDataSettings.languageBundle.getString("mainWindowsshowinvoicesfolderbuttonText"));
		this.settingsButton.setText(AppDataSettings.languageBundle.getString("mainWindowssettingsbuttonText"));
		this.emailConfigurationButton.setText(AppDataSettings.languageBundle.getString("mainWindowsemailconfigurationbuttonText"));
		this.unpaidInvoiceDate.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceDateHeader"));
		this.unpaidInvoiceSum.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceSumHeader"));
		this.unpaidInvoiceCurrencyIso.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceCurrencyisoHeader"));
		this.unpaidInvoiceCustomercompanyname.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceCompanynameHeader"));
		this.unpaidInvoiceCustomerforename.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceCustomerforenameHeader"));
		this.unpaidInvoiceCustomersurname.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceCustomersurnameHeader"));
		this.unpaidInvoiceCustomerpaybutton.setText(AppDataSettings.languageBundle.getString("unpaidinvoicePaidButtonHeader"));
		this.unpaidInvoiceCustomershowinvoicebutton.setText(AppDataSettings.languageBundle.getString("unpaidinvoiceShowinvoiceButtonHeader"));

	}

	@FXML
	private void handleNewClientAction(ActionEvent event) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/NewCustomer.fxml"));
			Stage addCustomerWindow = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");
			addCustomerWindow.setTitle(AppDataSettings.languageBundle.getString("addnewcustomerWindowHeader"));
			addCustomerWindow.setScene(scene);
			addCustomerWindow.show();
			NewCustomerController.newCustomerWindow = addCustomerWindow;
		} catch (IOException ex) {
			ErrorReport.reportException(ex);
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@FXML
	private void handleNewInvoiceAction(ActionEvent event) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/NewInvoice.fxml"));
			Stage addInvoiceWindow = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");
			addInvoiceWindow.setTitle(AppDataSettings.languageBundle.getString("addnewinvoiceWindowHeader"));
			addInvoiceWindow.setScene(scene);
			addInvoiceWindow.show();
			addInvoiceWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					NewInvoiceController.invoiceItemsData.clear();
				}
			});
			NewInvoiceController.addNewInvoiceWindow = addInvoiceWindow;

		} catch (Exception ex) {
			System.out.println(""+ErrorReport.reportException(ex));
			System.out.println("Error report: " + ex.getMessage());
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Load the language that the user chose through the combobox
	 *
	 * @param event
	 */
	@FXML
	public void handleLanguageChanged(ActionEvent event) {
		AppDataSettings settings = new AppDataSettings();
		this.settings.loadUIText(this.getKeyOfValue(this.langList, this.languageSelector.getValue().toString()));
		loadUiMainWindowsText();
	}

	/**
	 * Returns null if value is not available.
	 *
	 * @param map
	 * @param value
	 * @return
	 */
	public String getKeyOfValue(Map map, String value) {
		for (Object key : map.keySet()) {
			if ((map.get(key)).equals(value)) {
				return (String) key;
			}
		}
		return null;
	}

	/**
	 * Load data for table "unpaid invoices"
	 */
	public void loadUnpaidInvoices() {
		this.unpaidInvoicesList = this.settings.getUnpaidInvoices();
		this.unpaidInvoiceDate = new TableColumn();
		this.unpaidInvoiceSum = new TableColumn();
		this.unpaidInvoiceCurrencyIso = new TableColumn();
		this.unpaidInvoiceCustomercompanyname = new TableColumn();
		this.unpaidInvoiceCustomerforename = new TableColumn();
		this.unpaidInvoiceCustomersurname = new TableColumn();
		this.unpaidInvoiceCustomerpaybutton = new TableColumn<UnpaidInvoice, Object>();
		this.unpaidInvoiceCustomershowinvoicebutton = new TableColumn<UnpaidInvoice, Object>();
		this.unpaidInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		this.unpaidInvoiceSum.setCellValueFactory(new PropertyValueFactory<>("sum"));
		this.unpaidInvoiceCurrencyIso.setCellValueFactory(new PropertyValueFactory<>("currencyIso"));
		this.unpaidInvoiceCustomercompanyname.setCellValueFactory(new PropertyValueFactory<>("customercompanyname"));
		this.unpaidInvoiceCustomerforename.setCellValueFactory(new PropertyValueFactory<>("customerforename"));
		this.unpaidInvoiceCustomersurname.setCellValueFactory(new PropertyValueFactory<>("customersurname"));
		this.unpaidInvoiceCustomerpaybutton.setCellValueFactory(new PropertyValueFactory<>("paid"));
		this.unpaidInvoiceCustomershowinvoicebutton.setCellValueFactory(new PropertyValueFactory<>("showinvoice"));
		this.unpaidInvoicesData.addAll(this.unpaidInvoicesList);
		this.unpaidInvoices
				.setPlaceholder(new Label(AppDataSettings.languageBundle.getString("unpaidinvicesDefaultPlaceholder")));
		this.unpaidInvoices.setItems(this.unpaidInvoicesData);

		this.unpaidInvoices.getColumns().addAll(this.unpaidInvoiceDate, this.unpaidInvoiceSum,
				this.unpaidInvoiceCurrencyIso, this.unpaidInvoiceCustomercompanyname,
				this.unpaidInvoiceCustomerforename, this.unpaidInvoiceCustomersurname,
				this.unpaidInvoiceCustomerpaybutton, this.unpaidInvoiceCustomershowinvoicebutton);
	}

	/**
	 * Open Settings window for app settings
	 */
	@FXML
	public void handleOpenSettings() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/Settings.fxml"));
			Stage settingsWindow = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");
			settingsWindow.setTitle(AppDataSettings.languageBundle.getString("settingsWindowHeaderText"));
			settingsWindow.setScene(scene);
			settingsWindow.show();
			SettingsController.SETTINGSWINDOW = settingsWindow;

		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Open email Settings window for sending of invoice pdf mails
	 */
	@FXML
	public void handleOpenEmailConfiguration() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/EmailConfiguration.fxml"));
			Stage emailconfigurationWindow = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");
			emailconfigurationWindow.setTitle(AppDataSettings.languageBundle.getString("emailConfigurationWindowHeaderText"));
			emailconfigurationWindow.setScene(scene);
			emailconfigurationWindow.show();
			EmailConfigurationController.EMAILCONFIGURATIONWINDOW = emailconfigurationWindow;

		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@FXML
	public void handleShowInvoicesFolder() throws IOException {
		try {
			Desktop.getDesktop().browseFileDirectory(new File(AppDataSettings.INVOICESFILELOCATION));
		} catch (UnsupportedOperationException e) {
			// If other os (such as Windows) is used, then open through another command
			try {
				Desktop.getDesktop().open(new File(AppDataSettings.INVOICESFILELOCATION));
			} catch (Exception e2) {
				try {
					Desktop.getDesktop().edit(new File(AppDataSettings.INVOICESFILELOCATION));
				} catch (Exception e3) {
					System.out.println("Error - Invoices folder cannot be opened: " + e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
					alert.setContentText(AppDataSettings.languageBundle.getString("errorCannotOpenInvoiceFolder") + ": "
							+ AppDataSettings.INVOICESFILELOCATION);
					alert.show();

				}
			}
		}
	}

	public static void showInvoice(Integer invoiceid) {
		if (invoiceid > 0) {
			String invoiceFileLocation = AppDataSettings.INVOICESFILELOCATION + "/invoice_no" + invoiceid + ".pdf";
			try {
				Desktop.getDesktop().browseFileDirectory(new File(invoiceFileLocation));
			} catch (UnsupportedOperationException e) {
				// If other os (such as Windows) is used, then open through another command
				try {
					Desktop.getDesktop().open(new File(invoiceFileLocation));
				} catch (Exception e2) {
					try {
						Desktop.getDesktop().edit(new File(invoiceFileLocation));
					} catch (Exception e3) {
						System.out.println("Error - Invoices folder cannot be opened: " + e);
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
						alert.setContentText(AppDataSettings.languageBundle.getString("errorCannotOpenInvoiceFolder")
								+ ": " + AppDataSettings.INVOICESFILELOCATION);
						alert.show();
					}
				}
			}
		} else {
			System.out.println("Error - invoice id does not exist ");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorCannotOpenInvoiceFolder") + ": "
					+ AppDataSettings.INVOICESFILELOCATION);
			alert.show();
		}
	}
}
