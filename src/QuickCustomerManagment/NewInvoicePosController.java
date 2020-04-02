package QuickCustomerManagment;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.InvoicePos;

public class NewInvoicePosController implements Initializable {
	@FXML
	private TextField itemnameinvoiceposfield;
	@FXML
	private TextField itemunitinvoiceposfield;
	@FXML
	private TextField itempriceperunitinvoiceposfield;
	@FXML
	private TextField itemsuminvoiceposfield;

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
		loadPriceInputListeners();
		loadNewInvoicePosWindowText();
	}

	private void loadNewInvoicePosWindowText() {
		this.newinvoiceposHeader.setText(AppDataSettings.languageBundle.getString("invoiceposWindowTitleText"));
		this.itemnameinvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowItemnameText"));
		this.unitinvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowUnitText"));
		this.priceperunitinvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowPriceperunitText"));
		this.suminvoiceposdesc.setText(AppDataSettings.languageBundle.getString("invoiceposWindowSumText"));
		this.addinvoiceitemButton.setText(AppDataSettings.languageBundle.getString("invoiceposWindowAddButtonText"));
	}
	
	public void loadPriceInputListeners() {

		this.itempriceperunitinvoiceposfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				updateInvoicePosSumFORPricePerUnit(arg2);
			}
		});

		this.itemunitinvoiceposfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				updateInvoicePosSumFORUnit(arg2);
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
			Double unit = Double.parseDouble((itemunitinvoiceposfield.getText()));
			double priceperunit = Double.parseDouble((itempriceperunitinvoiceposfield.getText()).replace(',', '.'));
			double sum = Double.parseDouble((itemsuminvoiceposfield.getText()).replace(',', '.'));
			NewInvoiceController.invoiceItemsData
					.add(new InvoicePos(NewInvoiceController.INVOICEPOSID, itemnameinvoiceposfield.getText(), unit, priceperunit, sum));

			invoicePosWindow.close();
			NewInvoiceController.INVOICEPOSID++;

		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorInvoicePosNoNumber"));
			alert.show();

		}
	}

	/**
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

}
