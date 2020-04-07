package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import QuickCustomerManagment.AppDataSettings;
import QuickCustomerManagment.EditCustomerController;
import QuickCustomerManagment.EditProductsController;
import QuickCustomerManagment.ErrorReport;
import QuickCustomerManagment.MainWindowController;
import QuickCustomerManagment.ViewCustomersController;
import QuickCustomerManagment.ViewProductsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Products implements Serializable {

	private int id;
	private String productname;
	private double priceperunit;
	private Integer availableamount;
	private String note;
	private TextField priceperunitField;
	private TextField availableamountField;
	private Button edit;
	private Button delete;

	public Products(int id, String productname, double priceperunit, Integer availableamount, String note,
			String editButtonDescription, String deleteButtonDescription) {
		this.id = id;
		this.productname = productname;
		this.priceperunit = priceperunit;
		this.availableamount = availableamount;
		this.note = note;

		this.priceperunitField = new TextField();
		this.priceperunitField.setId(id + "");
		this.priceperunitField.setText("" + priceperunit);
		this.priceperunitField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				AppDataSettings updateDatabase = new AppDataSettings();

				TextField selectedTextField = (TextField) arg0.getSource();
				try {
					Integer editedProductsIdParsed = Integer.parseInt(selectedTextField.getId());
					Double newPriceperunitParsed = Double.parseDouble((selectedTextField.getText()).replace(",", "."));
					updateDatabase.updateProductsPriceperunitById(editedProductsIdParsed, newPriceperunitParsed);
					ViewProductsController.productsTableData.clear();
					ViewProductsController.productsTableData.addAll(updateDatabase.getAllProducts());
				} catch (NumberFormatException e) {
					System.out.println("" + e);
					// ErrorReport.reportException(e);
					if (selectedTextField.getText() != null && !selectedTextField.getText().equals("")
							&& !selectedTextField.getText().equals(" ")) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
						alert.setContentText(AppDataSettings.languageBundle.getString("allproductsNoNumber"));
						alert.show();
					}
				}
			}
		});

		this.availableamountField = new TextField();
		this.availableamountField.setId(id + "");
		this.availableamountField.setText("" + availableamount);
		this.availableamountField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				AppDataSettings updateDatabase = new AppDataSettings();
				TextField selectedTextField = (TextField) arg0.getSource();
				try {
					Integer editedProductsIdParsed = Integer.parseInt(selectedTextField.getId());
					Integer newAvailableamontParsed = Integer.valueOf(selectedTextField.getText());
					updateDatabase.updateProductsAvailableamountById(editedProductsIdParsed, newAvailableamontParsed);
					ViewProductsController.productsTableData.clear();
					ViewProductsController.productsTableData.addAll(updateDatabase.getAllProducts());
				} catch (NumberFormatException | NullPointerException e) {
					if (selectedTextField.getText() != null && !selectedTextField.getText().equals("")
							&& !selectedTextField.getText().equals(" ")) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
						alert.setContentText(AppDataSettings.languageBundle.getString("allproductsNoNumber"));
						alert.show();
					}
				}
			}
		});

		this.edit = new Button(editButtonDescription);
		this.edit.setId(id + "");
		this.edit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Button selectedButton = (Button) arg0.getSource();
				try {
					Integer clickedProductsIdParsed = Integer.parseInt(selectedButton.getId());
					Parent root;
					try {
						EditProductsController.productsId = clickedProductsIdParsed;

						root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/EditProducts.fxml"));
						Stage editproductsWindow = new Stage();
						EditProductsController.editProductsWindow = editproductsWindow;
						Scene scene = new Scene(root);
						scene.getStylesheets().add("/styles/Styles.css");
						editproductsWindow
								.setTitle(AppDataSettings.languageBundle.getString("editproductsWindowHeaderText"));
						editproductsWindow.setScene(scene);
						editproductsWindow.show();

					} catch (IOException ex) {
						Logger.getLogger(Products.class.getName()).log(Level.SEVERE, null, ex);
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
					Integer clickedProductsIdParsed = Integer.parseInt(selectedButton.getId());
					updateDatabase.deleteProductsById(clickedProductsIdParsed);
					ViewProductsController.productsTableData.clear();
					ViewProductsController.productsTableData.addAll(updateDatabase.getAllProducts());
				} catch (NumberFormatException e) {
					System.out.println("" + e);
					ErrorReport.reportException(e);
				}
			}

		});
	}

	public Products(int id, String productname, double priceperunit, Integer availableamount, String note) {
		this.id = id;
		this.productname = productname;
		this.priceperunit = priceperunit;
		this.availableamount = availableamount;
		this.note = note;
	}

	public Products(String productname, double priceperunit, Integer availableamount, String note) {
		this.productname = productname;
		this.priceperunit = priceperunit;
		this.availableamount = availableamount;
		this.note = note;
	}

	/**
	 * Add product with unlimited amount
	 * 
	 * @param productname
	 * @param priceperunit
	 * @param note
	 */
	public Products(String productname, double priceperunit, String note) {
		this.productname = productname;
		this.priceperunit = priceperunit;
		this.availableamount = -1;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public double getPriceperunit() {
		return priceperunit;
	}

	public void setPriceperunit(double priceperunit) {
		this.priceperunit = priceperunit;
	}

	public Integer getAvailableamount() {
		return availableamount;
	}

	public void setAvailableamount(Integer availableamount) {
		this.availableamount = availableamount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TextField getPriceperunitField() {
		return priceperunitField;
	}

	public void setPriceperunitField(TextField priceperunitField) {
		this.priceperunitField = priceperunitField;
	}

	public TextField getAvailableamountField() {
		return availableamountField;
	}

	public void setAvailableamountField(TextField availableamountField) {
		this.availableamountField = availableamountField;
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

}
