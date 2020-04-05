package model;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import QuickCustomerManagment.AppDataSettings;
import QuickCustomerManagment.EditCustomerController;
import QuickCustomerManagment.EmailConfigurationController;
import QuickCustomerManagment.ErrorReport;
import QuickCustomerManagment.MainWindowController;
import QuickCustomerManagment.ViewCustomersController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Customer {

	private Integer id;
	private String email;
	private String forename;
	private String lastname;
	private String companyname;
	private String taxnumber;
	private String street;
	private Integer zipcode;
	private String city;
	private String county;
	private String country;
	private String currencyiso;
	private String note;
	private Button edit;
	private Button delete;

	public Customer() {
	}

	public Customer(Integer id, String email, String forename, String lastname, String companyname, String street,
			String taxnumber, Integer zipcode, String city, String county, String country, String currencyiso,
			String note) {
		this.id = id;
		this.email = email;
		this.forename = forename;
		this.lastname = lastname;
		this.companyname = companyname;
		this.taxnumber = taxnumber;
		this.street = street;
		this.zipcode = zipcode;
		this.city = city;
		this.county = county;
		this.country = country;
		this.currencyiso = currencyiso;
		this.note = note;

	}

	public Customer(Integer id, String email, String forename, String lastname, String companyname, String street,
			String taxnumber, Integer zipcode, String city, String county, String country, String currencyiso,
			String note, String editButtonDescription, String deleteButtonDescription) {
		
		this(id, email, forename, lastname, companyname, street, taxnumber, zipcode, city, county, country, currencyiso,
				note);

		this.edit = new Button(editButtonDescription);
		this.edit.setId(id + "");
		this.edit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Button selectedButton = (Button) arg0.getSource();
				try {
					Integer clickedCustomerIdParsed = Integer.parseInt(selectedButton.getId());
					Parent root;
					try {
						EditCustomerController.customerId = clickedCustomerIdParsed;

						root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/EditCustomer.fxml"));
						Stage editcustomerWindow = new Stage();
						EditCustomerController.editCustomerWindow = editcustomerWindow;
						Scene scene = new Scene(root);
						scene.getStylesheets().add("/styles/Styles.css");
						editcustomerWindow
								.setTitle(AppDataSettings.languageBundle.getString("editcustomerWindowHeaderText"));
						editcustomerWindow.setScene(scene);
						editcustomerWindow.show();

					} catch (IOException ex) {
						Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
						ErrorReport.reportException(ex);

					}
				} catch (NumberFormatException e) {
					System.out.println("" + e);
					ErrorReport.reportException(e);
				}
			}

		});
		
		this.delete = new Button(deleteButtonDescription);
		this.delete.setId(id + "");
		this.delete.setOnAction(new EventHandler<ActionEvent>() {
			AppDataSettings updateDatabase = new AppDataSettings();

			@Override
			public void handle(ActionEvent arg0) {
				Button selectedButton = (Button) arg0.getSource();
				try {
					Integer clickedCustomerIdParsed = Integer.parseInt(selectedButton.getId());
					updateDatabase.deleteCustomerById(clickedCustomerIdParsed);
					ViewCustomersController.customersTableData.clear();
					ViewCustomersController.customersTableData.addAll(updateDatabase.getAllCustomers());
				} catch (NumberFormatException e) {
					System.out.println("" + e);
					ErrorReport.reportException(e);
				}
			}

		});

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyiso() {
		return currencyiso;
	}

	public void setCurrencyiso(String currencyiso) {
		this.currencyiso = currencyiso;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTaxnumber() {
		return taxnumber;
	}

	public void setTaxnumber(String taxnumber) {
		this.taxnumber = taxnumber;
	}

	public Button getEdit() {
		return edit;
	}

	public void setEdit(Button edit) {
		this.edit = edit;
	}

	public Button getDelete() {
		return delete;
	}

	public void setDelete(Button delete) {
		this.delete = delete;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + Objects.hashCode(this.id);
		hash = 53 * hash + Objects.hashCode(this.forename);
		hash = 53 * hash + Objects.hashCode(this.lastname);
		hash = 53 * hash + Objects.hashCode(this.companyname);
		hash = 53 * hash + Objects.hashCode(this.street);
		hash = 53 * hash + Objects.hashCode(this.zipcode);
		hash = 53 * hash + Objects.hashCode(this.city);
		hash = 53 * hash + Objects.hashCode(this.county);
		hash = 53 * hash + Objects.hashCode(this.country);
		hash = 53 * hash + Objects.hashCode(this.currencyiso);
		hash = 53 * hash + Objects.hashCode(this.note);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Customer other = (Customer) obj;
		if (!Objects.equals(this.forename, other.forename)) {
			return false;
		}
		if (!Objects.equals(this.lastname, other.lastname)) {
			return false;
		}
		if (!Objects.equals(this.companyname, other.companyname)) {
			return false;
		}
		if (!Objects.equals(this.street, other.street)) {
			return false;
		}
		if (!Objects.equals(this.city, other.city)) {
			return false;
		}
		if (!Objects.equals(this.county, other.county)) {
			return false;
		}
		if (!Objects.equals(this.country, other.country)) {
			return false;
		}
		if (!Objects.equals(this.currencyiso, other.currencyiso)) {
			return false;
		}
		if (!Objects.equals(this.note, other.note)) {
			return false;
		}
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.zipcode, other.zipcode)) {
			return false;
		}
		return true;
	}

}
