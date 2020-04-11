package QuickCustomerManagment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import model.Products;

public class ViewProductsController implements Initializable {

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
	public Text productsAmountText = new Text();
	public static List productsList;
	public int productsAmount = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		productsList = loadData.getAllProducts();
		loadProducts();

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
		loadProductsTableListener();

	}

	private void loadWindowText() {
		this.WindowHeaderTitle.setText(AppDataSettings.languageBundle.getString("allproductsWindowHeaderTxt"));
		this.productsId.setText(AppDataSettings.languageBundle.getString("allproductsWindowIdTableTxt"));
		this.productsProductname
				.setText(AppDataSettings.languageBundle.getString("allproductsWindowProductnameTableTxt"));
		this.productsPriceperunit
				.setText(AppDataSettings.languageBundle.getString("allproductsWindowPriceperunitTableTxt"));
		this.productsAvailableamount
				.setText(AppDataSettings.languageBundle.getString("allproductsWindowAvailableamountTableTxt"));
		this.productsEditButton.setText(AppDataSettings.languageBundle.getString("allproductsWindowEditButtonTxt"));
		this.productsDeleteButton.setText(AppDataSettings.languageBundle.getString("allproductsWindowDeleteButtonTxt"));
	}

	public void loadProducts() {

		if (productsList != null && productsList.size() > 0) {
			this.productsAmount = productsList.size();
			this.productsAmountText.setText(AppDataSettings.languageBundle
					.getString("allproductsWindowProductsAmountTxt").replace("--%--", "" + this.productsAmount));
			productsTableData.addAll(productsList);

		} else {
			this.productsAmountText.setText("Developed by a.dridi (github.com/a-dridi)");
		}
		
	}
	
	/**
	 * Reload products table after editing a table row
	 */
	public static void reloadProducts() {
		productsTableData.clear();
		AppDataSettings reloadData = new AppDataSettings();
		productsList = reloadData.getAllProducts();
		productsTableData.addAll(productsList);
	}

	public void loadProductsTableListener() {
		this.productsTableData.addListener(new ListChangeListener() {

			@Override
			public void onChanged(Change arg0) {
				reloadDeletedProductsAmountText();
			}

		});
	}

	public void reloadDeletedProductsAmountText() {
		try {
			this.productsAmount = this.loadData.getAllProducts().size();
			this.productsAmountText.setText(AppDataSettings.languageBundle.getString("allproductsWindowProductsAmountTxt")
					.replace("--%--", "" + this.productsAmount));
		} catch (NullPointerException e) {

		}
	}

}
