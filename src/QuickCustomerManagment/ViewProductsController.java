package QuickCustomerManagment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Customer;
import model.Products;

public class ViewProductsController implements Initializable{

	private AppDataSettings loadData = new AppDataSettings();

	@FXML
	private TableView productsTable = new TableView();

	public static ObservableList<Products> productsTableData = FXCollections.observableArrayList();

	public TableColumn productsId;
	public TableColumn productsProductname;
	public TableColumn productsPriceperunit;
	public TableColumn productsAvailableamount;
	public TableColumn productsEditButton;
	public TableColumn productsDeleteButton;

	@FXML
	private Text WindowHeaderTitle = new Text();
	@FXML
	private Text productsAmountText = new Text();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List productsList = null;

		productsList = loadData.getAllProducts();

		if (productsList != null && productsList.size() > 0) {
			this.productsAmountText.setText(AppDataSettings.languageBundle
					.getString("allproductsWindowProductsAmountTxt").replace("--%--", "" + productsList.size()));
			productsTableData.addAll(productsList);

		} else {
			this.productsAmountText.setText(" - ");
		}

		this.productsId = new TableColumn();
		this.productsProductname = new TableColumn();
		this.productsPriceperunit = new TableColumn<Products, Object>();
		this.productsAvailableamount = new TableColumn<Products, Object>();
		this.productsEditButton = new TableColumn<Products, Object>();
		this.productsDeleteButton = new TableColumn<Products, Object>();
		this.productsId.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.productsProductname.setCellValueFactory(new PropertyValueFactory<>("productname"));
		this.productsPriceperunit.setCellValueFactory(new PropertyValueFactory<>("priceperunitField"));
		this.productsAvailableamount.setCellValueFactory(new PropertyValueFactory<>("availableamountField"));
		this.productsEditButton.setCellValueFactory(new PropertyValueFactory<>("edit"));
		this.productsDeleteButton.setCellValueFactory(new PropertyValueFactory<>("delete"));

		this.productsTable.setPlaceholder(
				new Label(AppDataSettings.languageBundle.getString("allproductsWindowProductsNoAmountTxt")));
		this.productsTable.setItems(productsTableData);
		this.productsTable.getColumns().addAll(this.productsId, this.productsProductname, this.productsPriceperunit,
				this.productsAvailableamount, this.productsEditButton, this.productsDeleteButton);
		
		loadWindowText();
	}
	
	private void loadWindowText() {
		this.WindowHeaderTitle.setText(AppDataSettings.languageBundle.getString("allproductsWindowHeaderTxt"));
		this.productsId.setText(AppDataSettings.languageBundle.getString("allproductsWindowIdTableTxt"));
		this.productsProductname
				.setText(AppDataSettings.languageBundle.getString("allproductsWindowProductnameTableTxt"));
		this.productsPriceperunit.setText(AppDataSettings.languageBundle.getString("allproductsWindowPriceperunitTableTxt"));
		this.productsAvailableamount.setText(AppDataSettings.languageBundle.getString("allproductsWindowAvailableamountTableTxt"));
		this.productsEditButton.setText(AppDataSettings.languageBundle.getString("allproductsWindowEditButtonTxt"));
		this.productsDeleteButton.setText(AppDataSettings.languageBundle.getString("allproductsWindowDeleteButtonTxt"));
	}

}
