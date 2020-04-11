package QuickCustomerManagment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Customer;
import model.UnpaidInvoice;

public class ViewCustomersController implements Initializable {

	private AppDataSettings loadData = new AppDataSettings();

	@FXML
	private TableView customersTable = new TableView();

	public static ObservableList<Customer> customersTableData = FXCollections.observableArrayList();
	private int customerAmount;
	public TableColumn customerId;
	public TableColumn customerCompanyname;
	public TableColumn customerForename;
	public TableColumn customerSurname;
	public TableColumn customerEmail;
	public TableColumn customerEditButton;
	public TableColumn customerDeleteButton;

	@FXML
	private Text WindowHeaderTitle = new Text();
	@FXML
	private Text customerAmountText = new Text();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List customersList = null;

		customersList = loadData.getAllCustomers();

		if (customersList != null && customersList.size() > 0) {
			this.customerAmount = customersList.size();
			this.customerAmountText.setText(AppDataSettings.languageBundle
					.getString("allcustomersWindowCustomerAmountTxt").replace("--%--", "" + this.customerAmount));
			customersTableData.addAll(customersList);

		} else {
			this.customerAmountText.setText(" - ");
		}

		this.customerId = new TableColumn();
		this.customerCompanyname = new TableColumn();
		this.customerForename = new TableColumn();
		this.customerSurname = new TableColumn();
		this.customerEmail = new TableColumn();
		this.customerEditButton = new TableColumn<Customer, Object>();
		this.customerDeleteButton = new TableColumn<Customer, Object>();
		this.customerId.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.customerCompanyname.setCellValueFactory(new PropertyValueFactory<>("companyname"));
		this.customerForename.setCellValueFactory(new PropertyValueFactory<>("forename"));
		this.customerSurname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
		this.customerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		this.customerEditButton.setCellValueFactory(new PropertyValueFactory<>("edit"));
		this.customerDeleteButton.setCellValueFactory(new PropertyValueFactory<>("delete"));

		this.customersTable.setPlaceholder(
				new Label(AppDataSettings.languageBundle.getString("allcustomersWindowCustomerNoAmountTxt")));
		this.customersTable.setItems(customersTableData);
		this.customersTable.getColumns().addAll(this.customerId, this.customerCompanyname, this.customerForename,
				this.customerSurname, this.customerEmail, this.customerEditButton, this.customerDeleteButton);

		loadCustomersTableListener();
		loadWindowText();
	}

	private void loadWindowText() {
		this.WindowHeaderTitle.setText(AppDataSettings.languageBundle.getString("allcustomersWindowHeaderTxt"));
		this.customerId.setText(AppDataSettings.languageBundle.getString("allcustomersWindowIdTableTxt"));
		this.customerCompanyname
				.setText(AppDataSettings.languageBundle.getString("allcustomersWindowCompanynameTableTxt"));
		this.customerForename.setText(AppDataSettings.languageBundle.getString("allcustomersWindowForenameTableTxt"));
		this.customerSurname.setText(AppDataSettings.languageBundle.getString("allcustomersWindowSurenameTableTxt"));
		this.customerEmail.setText(AppDataSettings.languageBundle.getString("allcustomersWindowEmailTableTxt"));
		this.customerEditButton.setText(AppDataSettings.languageBundle.getString("allcustomersWindowEditButtonTxt"));
		this.customerDeleteButton
				.setText(AppDataSettings.languageBundle.getString("allcustomersWindowDeleteButtonTxt"));
	}

	public void loadCustomersTableListener() {
		this.customersTableData.addListener(new ListChangeListener() {

			@Override
			public void onChanged(Change arg0) {
				reloadDeletedCustomerAmountText();
			}

		});
	}

	public void reloadDeletedCustomerAmountText() {
		try {
			this.customerAmount = this.loadData.getAllCustomers().size();
			this.customerAmountText.setText(AppDataSettings.languageBundle.getString("allcustomersWindowCustomerAmountTxt")
					.replace("--%--", "" + this.customerAmount));
		} catch (NullPointerException e) {

		}
	}
	
	/**
	 * Reload customers table after editing a table row
	 */
	public static void reloadCustomers() {
		customersTableData.clear();
		try {
			AppDataSettings reloadData = new AppDataSettings();
		customersTableData.addAll(reloadData.getAllCustomers());
		}catch(NullPointerException e) {
			
		}
	}

}
