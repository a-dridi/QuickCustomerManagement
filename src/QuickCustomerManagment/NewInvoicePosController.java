package QuickCustomerManagment;

import javafx.event.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.InvoicePos;
import model.Products;

public class NewInvoicePosController implements Initializable {

	@FXML
	private ComboBox productTemplateSelect;
	// Maps the value that is displayed in the combobox with the actual object
	public static Map<String, Products> productsObjectTemplateString = new TreeMap<>();

	@FXML
	private TextField itemnameinvoiceposfield;
	@FXML
	private TextField itemunitinvoiceposfield;
	@FXML
	private TextField itempriceperunitinvoiceposfield;
	@FXML
	private TextField itemsuminvoiceposfield;

	@FXML
	private Text templateDesc = new Text();
	@FXML
	private Text newinvoiceposHeader = new Text();
	@FXML
	private Text itemnameinvoiceposdesc = new Text();
	@FXML
	private Text unitinvoiceposdesc = new Text();
	@FXML
	private Text priceperunitinvoiceposdesc = new Text();
	@FXML
	private Text suminvoiceposdesc = new Text();
	@FXML
	private Button addinvoiceitemButton = new Button();

	private AppDataSettings loadData;
	public static Integer invoiceId;
	public static Stage invoicePosWindow;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadData = new AppDataSettings();

		loadProductsTemplateCombobox();
		loadPriceInputListeners();
		loadProductTemplateSelectListener();
		loadNewInvoicePosWindowText();
	}

	private void loadNewInvoicePosWindowText() {
		this.templateDesc.setText(AppDataSettings.languageBundle.getString("invoiceposTemplateDescText"));
		this.newinvoiceposHeader.setText(AppDataSettings.languageBundle.getString("invoiceposWindowTitleText"));
		this.itemnameinvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowItemnameText"));
		this.unitinvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowUnitText"));
		this.priceperunitinvoiceposdesc
				.setText(AppDataSettings.languageBundle.getString("invoiceposWindowPriceperunitText"));
		this.suminvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowSumText"));
		this.addinvoiceitemButton.setText(AppDataSettings.languageBundle.getString("invoiceposWindowAddButtonText"));
	}

	public void loadProductsTemplateCombobox() {
		// Load combobox with customized display string
		productsObjectTemplateString.clear();
		this.productTemplateSelect.getItems().clear();
		for (Products product : loadData.getAllProducts()) {
			StringBuilder displayString = new StringBuilder();
			if (product.getAvailableamount() == -1) {
				displayString.append(product.getProductname());
				displayString.append(" - ");
				displayString.append(AppDataSettings.languageBundle.getString("invoiceposUnlimitedamountText"));
			} else {
				displayString.append(product.getProductname());
				displayString.append(" - ");
				displayString.append(product.getAvailableamount());
				displayString.append(" x");
			}
			productsObjectTemplateString.put(displayString.toString(), product);
		}
		for (String displayString : this.productsObjectTemplateString.keySet()) {
			this.productTemplateSelect.getItems().add(displayString);
		}
	}

	public void loadPriceInputListeners() {

		this.itempriceperunitinvoiceposfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableObject, String oldValue, String newValue) {
				updateInvoicePosSumFORPricePerUnit(newValue);
			}
		});

		this.itemunitinvoiceposfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableObject, String oldValue, String newValue) {
				updateInvoicePosSumFORUnit(newValue);
			}
		});
	}

	public void loadProductTemplateSelectListener() {
		this.productTemplateSelect.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observableObject, String oldValue, String newValue) {
				Products selectedProduct = NewInvoicePosController.productsObjectTemplateString.get(newValue);
				updateInvoicePosItemname(selectedProduct.getProductname());
				updateInvoicePosItemprice("" + selectedProduct.getPriceperunit());
			}

		});
	}

	/**
	 * Call this method and pass the invoice id before you open the InvoicePos
	 * Window
	 */
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * The opened window for adding new invoice item
	 */
	public void setStageWindow(Stage invoicePosWindow) {
		this.invoicePosWindow = invoicePosWindow;
	}

	@FXML
	public void handleSaveInvoicePos(ActionEvent event) {

		try {
			Integer unit = Integer.valueOf((itemunitinvoiceposfield.getText()));
			double priceperunit = Double.parseDouble((itempriceperunitinvoiceposfield.getText()).replace(',', '.'));
			double sum = Double.parseDouble((itemsuminvoiceposfield.getText()).replace(',', '.'));
			NewInvoiceController.invoiceItemsData.add(new InvoicePos(NewInvoiceController.INVOICEPOSID,
					itemnameinvoiceposfield.getText(), unit, priceperunit, sum));

			invoicePosWindow.close();
			NewInvoiceController.INVOICEPOSID++;
			this.productsObjectTemplateString.clear();

		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorInvoicePosNoNumber"));
			alert.show();

		}
	}

	/**
	 * Changes the text field "sum" to newValue. Used for ChangeListener of
	 * "pricePerUnit" text field
	 * 
	 * Invoice sum calculated in cent, then convert to currency unit to display.
	 */

	public void updateInvoicePosSumFORPricePerUnit(String newValue) {
		try {
			if (this.itemunitinvoiceposfield.getText() != null && newValue != null) {
				double unit = 0.;
				double priceperunit = 0.;
				unit = Integer.parseInt(itemunitinvoiceposfield.getText());

				if ((newValue).contains(".") || (newValue).contains(",")) {

					// parse double
					priceperunit = Double.parseDouble(("" + (newValue).replace(',', '.')));
				} else {
					// Parse int
					// this.itempriceperunitinvoiceposfield.getT
					priceperunit = Integer.parseInt(newValue);
				}
				int priceperunitCent = (int) (priceperunit * 100);
				itemsuminvoiceposfield.setText("" + ((Double) ((unit * priceperunitCent) / 100)));
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception: " + e);
		}
	}

	/**
	 * Changes the text field "sum" to newValue. Used for ChangeListener of "unit"
	 * text field
	 * 
	 * @param newValue
	 */
	public void updateInvoicePosSumFORUnit(String newValue) {
		try {
			if (this.itemunitinvoiceposfield.getText() != null && newValue != null) {
				double unit = 0.;
				double priceperunit = 0.;
				unit = Integer.parseInt(newValue);

				if ((this.itempriceperunitinvoiceposfield.getText()).contains(".")
						|| (this.itempriceperunitinvoiceposfield.getText()).contains(",")) {

					// parse double
					priceperunit = Double
							.parseDouble((this.itempriceperunitinvoiceposfield.getText()).replace(',', '.'));
				} else {
					// Parse int
					// this.itempriceperunitinvoiceposfield.getT
					priceperunit = Integer.parseInt(this.itempriceperunitinvoiceposfield.getText());
				}
				int priceperunitCent = (int) (priceperunit * 100);
				itemsuminvoiceposfield.setText("" + ((Double) ((unit * priceperunitCent) / 100)));
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception: " + e);
		}
	}

	/**
	 * Update text field "item name". Used for ChangeListener of combobox
	 * "template".
	 * 
	 * @param itemname
	 */
	public void updateInvoicePosItemname(String newValue) {
		this.itemnameinvoiceposfield.setText(newValue);
	}

	/**
	 * Update text field "item price" (Price per unit). Used for ChangeListener of
	 * combobox "template".
	 * 
	 * @param itemname
	 */
	public void updateInvoicePosItemprice(String newValue) {
		this.itempriceperunitinvoiceposfield.setText(newValue);
	}

	@FXML
	public void handleOpenAddProductsWindow() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/AddProducts.fxml"));
			Stage addproductsWindow = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");
			addproductsWindow.setTitle(AppDataSettings.languageBundle.getString("addProductsWindowHeaderTxt"));
			addproductsWindow.setScene(scene);
			addproductsWindow.getIcons().add(new javafx.scene.image.Image("/QuickCustomerManagment/img/plus-square.png"));
			addproductsWindow.setResizable(false);
			addproductsWindow.setOnHidden(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					loadProductsTemplateCombobox();

				}
			});
			addproductsWindow.show();
			AddProductsController.addProductsWindow = addproductsWindow;

		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
