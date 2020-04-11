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
import model.Customer;
import model.InvoicePos;
import model.Products;

public class EditProductsController implements Initializable {

	@FXML
	private Text editProductsHeader = new Text();
	@FXML
	private Text productnameEditProductsDesc = new Text();
	@FXML
	private Text pricerperunitEditProductsDesc = new Text();
	@FXML
	private Text availableamountEditProductsDesc = new Text();
	@FXML
	private Text noteEditProductsDesc = new Text();

	@FXML
	private TextField productnameEditProductsField;
	@FXML
	private TextField pricerperunitEditProductsField;
	@FXML
	private TextField availableamountEditProductsField;
	@FXML
	private TextField noteEditProductsField;
	@FXML
	private Button SaveEditProductsButton = new Button();
	@FXML
	private Text windowInfoEditProductsText = new Text();
	
	private AppDataSettings loadData;
	public static Stage editProductsWindow;

	public static Integer productsId;
	private Products editedProducts;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadData = new AppDataSettings();
		this.editedProducts = loadData.getProductsById(productsId);
		loadEditProductsWindowText();
		loadFields();
	}

	private void loadEditProductsWindowText() {
		this.editProductsHeader.setText(AppDataSettings.languageBundle.getString("editproductsWindowHeaderText"));
		this.productnameEditProductsDesc
				.setText(AppDataSettings.languageBundle.getString("editProductsWindowProductnameTxt"));
		this.pricerperunitEditProductsDesc
				.setText(AppDataSettings.languageBundle.getString("editProductsWindowPriceperunitTxt"));
		this.availableamountEditProductsDesc
				.setText(AppDataSettings.languageBundle.getString("editProductsWindowAvailableAmountTxt"));
		this.noteEditProductsDesc.setText(AppDataSettings.languageBundle.getString("editProductsWindowNoteTxt"));
		this.SaveEditProductsButton.setText(AppDataSettings.languageBundle.getString("editProductsWindowSaveButtonTxt"));
		this.windowInfoEditProductsText.setText(AppDataSettings.languageBundle.getString("editProductsWindowWindowInfoTxt"));
	}

	private void loadFields() {
		this.productnameEditProductsField.setText(this.editedProducts.getProductname());
		this.pricerperunitEditProductsField.setText(""+this.editedProducts.getPriceperunit());
		this.availableamountEditProductsField.setText(""+this.editedProducts.getAvailableamount());
		this.noteEditProductsField.setText(this.editedProducts.getNote());
	}
	
	
	
	@FXML
	public void handleSaveEditProducts(ActionEvent event) {
		double priceperunit;
		Integer availableamount;
		try {
			priceperunit = Double.parseDouble((this.pricerperunitEditProductsField.getText().replace(',', '.')));
			availableamount = Integer.valueOf(this.availableamountEditProductsField.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorAddProductsNoNumber"));
			alert.show();
			return;
		}
		
		if (loadData.updateProducts(productsId,this.productnameEditProductsField.getText(), priceperunit, availableamount, this.noteEditProductsField.getText())) {
			this.editProductsWindow.close();
			ViewProductsController.reloadProducts();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(AppDataSettings.languageBundle.getString("okWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("okWindowProductsEdited"));
			alert.show();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(loadData.getErrorMessage());
			alert.show();
		}
	}
	
}
