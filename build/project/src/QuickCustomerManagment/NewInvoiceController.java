package QuickCustomerManagment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Stream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import model.InvoicePos;

public class NewInvoiceController implements Initializable {

	private Customer selectedInvoiceCustomer;

	@FXML
	private ComboBox countryinvoicecombobox;
	@FXML
	private ComboBox currencyinvoicecombobox;
	@FXML
	private ComboBox customerinvoicecombobox;

	@FXML
	private TextField taxnumberinvoicefield = new TextField();
	@FXML
	private TextField streetinvoicefield = new TextField();
	@FXML
	private TextField zipcodeinvoicefield = new TextField();
	@FXML
	private TextField cityinvoicefield = new TextField();
	@FXML
	private TextField countyinvoicefield = new TextField();
	private String customerCompanyname;
	private String customerForeSurname;
	private String customerFirstname;
	private String customerLastname;

	@FXML
	private TableView invoiceItems = new TableView();

	@FXML
	private TextField invoicetaxpctfield = new TextField();
	@FXML
	private Double taxpct = 0.0;

	@FXML
	private Text customerinvoicedesc = new Text();
	@FXML
	private Text taxnumbernvoicedesc = new Text();
	@FXML
	private Text streetinvoicedesc = new Text();
	@FXML
	private Text zipcodeinvoicedesc = new Text();
	@FXML
	private Text cityinvoicedesc = new Text();
	@FXML
	private Text countyinvoicedesc = new Text();
	@FXML
	private Text countryinvoicedesc = new Text();
	@FXML
	private Text noteinvoicedesc = new Text();
	@FXML
	private Text invoicetaxvalueheader = new Text();
	@FXML
	private Text currencyinvoicedesc = new Text();
	@FXML
	private Text invoicenetsumheader = new Text();
	@FXML
	private Text invoicetaxpctheader = new Text();
	@FXML
	private Text suminvoicedesc = new Text();
	@FXML
	private Button addinvoiceposbutton = new Button();
	@FXML
	private Button createinvoicebutton = new Button();

	@FXML
	private Text suminvoicefield;

	@FXML
	private Text invoicenetsumfield;

	@FXML
	private Text invoicetaxvaluefield;

	@FXML
	private TextArea noteinvoicefield = new TextArea();

	private TableColumn invoiceItemsColumnItemId;
	private TableColumn invoiceItemsColumnItemName;
	private TableColumn invoiceItemsColumnItemUnit;
	private TableColumn invoiceItemsColumnItemPriceperunit;
	private TableColumn invoiceItemsColumnItemSum;
	private TableColumn<InvoicePos, InvoicePos> invoiceItemsColumnItemDeletebutton;

	public static ObservableList<InvoicePos> invoiceItemsData = FXCollections.observableArrayList();

	private AppDataSettings loadData = new AppDataSettings();
	public static int INVOICEID;
	public static int INVOICEPOSID;

	public static int CUSTOMERID = 0;
	public static Stage addNewInvoiceWindow;
	private int areaCodeParsed;
	private Double netsumParsed = 0.;
	private Double taxvalueParsed = 0.;
	private Double sumParsed = 0.;
	private String createdInvoicePDFPath;
	public static boolean invoiceCreationProcessEnded;

	public NewInvoiceController() {
		this.loadData.loadAppSettings();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		INVOICEID = loadData.loadNewInvoiceId();
		INVOICEPOSID = 1;
		loadData.loadCurrenciesCountries();
		loadData.loadAppSettings();

		invoicetaxpctfield.setText("" + taxpct);
		invoiceItemsColumnItemId = new TableColumn();
		invoiceItemsColumnItemName = new TableColumn();
		invoiceItemsColumnItemUnit = new TableColumn();
		invoiceItemsColumnItemPriceperunit = new TableColumn();
		invoiceItemsColumnItemSum = new TableColumn();
		invoiceItemsColumnItemDeletebutton = new TableColumn(" ");

		invoiceItemsColumnItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
		invoiceItemsColumnItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
		invoiceItemsColumnItemUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
		invoiceItemsColumnItemPriceperunit.setCellValueFactory(new PropertyValueFactory<>("priceperunit"));
		invoiceItemsColumnItemSum.setCellValueFactory(new PropertyValueFactory<>("sumprice"));
		invoiceItemsColumnItemDeletebutton.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		invoiceItemsColumnItemDeletebutton.setCellFactory(param -> new TableCell<InvoicePos, InvoicePos>() {
			private final Button deleteButton = new Button(
					AppDataSettings.languageBundle.getString("invoicePosDeleteButtonHeader"));

			@Override
			protected void updateItem(InvoicePos invoicepos, boolean empty) {
				super.updateItem(invoicepos, empty);

				if (invoicepos == null) {
					setGraphic(null);
					return;
				}
				setGraphic(deleteButton);
				deleteButton.setOnAction(event -> {
					if (invoicepos.getId() == ((NewInvoiceController.INVOICEPOSID - 1))) {
						NewInvoiceController.INVOICEPOSID--;
					}
					getTableView().getItems().remove(invoicepos);
				});

			}
		});

		invoiceItems.setItems(invoiceItemsData);
		invoiceItems.setPlaceholder(
				new Label(AppDataSettings.languageBundle.getString("invoicepositemsDefaultPlaceholder")));

		invoiceItems.getColumns().addAll(invoiceItemsColumnItemId, invoiceItemsColumnItemName,
				invoiceItemsColumnItemUnit, invoiceItemsColumnItemPriceperunit, invoiceItemsColumnItemSum,
				invoiceItemsColumnItemDeletebutton);

		this.countryinvoicecombobox.getItems().addAll(loadData.getCountries());
		for (String currency : loadData.getCurrencies().keySet()) {
			this.currencyinvoicecombobox.getItems().add(currency);
		}
		this.currencyinvoicecombobox.getSelectionModel().select(loadData.getAppsettings().get("defaultCurrencyIso"));

		List<Customer> customerDataList = loadData.getAllCustomers();
		Map<String, Customer> viewCustomerMap = new HashMap();
		for (Customer c : customerDataList) {
			if (!c.getCompanyname().equals("")) {
				viewCustomerMap.put(c.getCompanyname() + " - " + c.getForename() + " " + c.getLastname(), c);
				this.customerinvoicecombobox.getItems()
						.add(c.getCompanyname() + " - " + c.getForename() + " " + c.getLastname());
			} else {
				viewCustomerMap.put(c.getForename() + " " + c.getLastname(), c);
				this.customerinvoicecombobox.getItems().add(c.getForename() + " " + c.getLastname());
			}
		}

		this.customerinvoicecombobox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (!arg2.equals("") && !arg2.equals(" ")) {
					Customer selectedCustomer = viewCustomerMap.get(arg2);
					CUSTOMERID = selectedCustomer.getId();
					setCustomerDetails(selectedCustomer);
				}
			}

		});
		// ChangeListeners
		loadListeners();
		loadNewInvoiceWindowText();

	}

	private void loadNewInvoiceWindowText() {
		this.invoiceItemsColumnItemId.setText(AppDataSettings.languageBundle.getString("invoiceItemColumnId"));
		this.invoiceItemsColumnItemName.setText(AppDataSettings.languageBundle.getString("invoiceItemColumnName"));
		this.invoiceItemsColumnItemUnit.setText(AppDataSettings.languageBundle.getString("invoiceItemColumnUnit"));
		this.invoiceItemsColumnItemPriceperunit
				.setText(AppDataSettings.languageBundle.getString("invoiceItemColumnPriceperunit"));
		this.invoiceItemsColumnItemSum.setText(AppDataSettings.languageBundle.getString("invoiceItemColumnSum"));
		this.customerinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowCustomerText"));
		this.taxnumbernvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowTaxnoText"));
		this.streetinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowStreetText"));
		this.zipcodeinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowZipcodeText"));
		this.cityinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowCityText"));
		this.countyinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowCountyText"));
		this.countryinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowCountryText"));
		this.invoicenetsumheader.setText(AppDataSettings.languageBundle.getString("invoiceWindowNetsumText"));
		this.invoicetaxpctheader.setText(AppDataSettings.languageBundle.getString("invoiceWindowTaxpctText"));
		this.invoicetaxvalueheader.setText(AppDataSettings.languageBundle.getString("invoiceWindowTaxesText"));
		this.suminvoicedesc.setText("= " + AppDataSettings.languageBundle.getString("invoiceWindowInvoicesumText"));
		this.currencyinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowCurrencyText"));
		this.noteinvoicedesc.setText(AppDataSettings.languageBundle.getString("invoiceWindowNoteText"));
		this.addinvoiceposbutton.setText(AppDataSettings.languageBundle.getString("invoiceWindowAddButtonText"));
		this.createinvoicebutton.setText(AppDataSettings.languageBundle.getString("invoiceWindowCreateButtonText"));

	}

	/**
	 * Sets the text fields for a selected customer with saved data of the customer
	 * table
	 */
	private void setCustomerDetails(Customer selectedCustomer) {
		this.selectedInvoiceCustomer = selectedCustomer;
		System.out.println("Email: " + selectedCustomer.getEmail());
		this.customerCompanyname = selectedCustomer.getCompanyname();
		this.customerForeSurname = selectedCustomer.getForename() + " " + selectedCustomer.getLastname();
		this.customerFirstname = selectedCustomer.getForename();
		this.customerLastname = selectedCustomer.getLastname();
		this.taxnumberinvoicefield.setText(selectedCustomer.getTaxnumber());
		this.streetinvoicefield.setText(selectedCustomer.getStreet());
		this.zipcodeinvoicefield.setText("" + selectedCustomer.getZipcode());
		this.cityinvoicefield.setText(selectedCustomer.getCity());
		this.countyinvoicefield.setText(selectedCustomer.getCounty());
		this.countryinvoicecombobox.getSelectionModel().select(selectedCustomer.getCountry());
		this.currencyinvoicecombobox.getSelectionModel().select(selectedCustomer.getCurrencyiso());
	}

	public void loadListeners() {

		this.invoicetaxpctfield.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				refreshInvoiceSumsAllForTaxPct(arg2);
			}

		});

		invoiceItemsData.addListener((ListChangeListener<InvoicePos>) c -> {
			calculateInvoiceSumsAll();
		});
	}

	/**
	 * Save invoice items in database and create invoice (and save it into the db)
	 * and send them through mail to email address of client
	 */
	@FXML
	public void handleCreateInvoice() {

		if (CUSTOMERID == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errorNewInvoiceNoCustomerSelected"));
			alert.show();
			return;

		} else {

			this.areaCodeParsed = 0;
			try {
				this.areaCodeParsed = Integer.parseInt(this.zipcodeinvoicefield.getText());
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
				alert.setContentText(AppDataSettings.languageBundle.getString("errorNewInvoiceNoAreaCode"));
				alert.show();
				return;
			}

			// Invoice creation process
			createInvoiceItems();
			createInvoiceDatabase();
			createInvoicePdf();
			createInvoiceSendEmail();

		}
	}

	/**
	 * Part of the invoice creation process. Add invoice items into the database,
	 */
	public void createInvoiceItems() {

		for (InvoicePos item : invoiceItemsData) {
			loadData.addNewInvoicePosItem(item.getId(), item.getItemname(), item.getUnit(), item.getPriceperunit(),
					item.getSumprice(), INVOICEID);
			loadData.changeProductsAvailableamountByProductname(item.getItemname(), -(item.getUnit()));
		}

	}

	/**
	 * Part of the invoice creation process. Must run after "createInvoiceItems".
	 * Add invoice in to the database and close "add invoice" window.
	 */
	public void createInvoiceDatabase() {
		// Generated values through "InvoicePos"

		try {
			netsumParsed = Double.valueOf((this.invoicenetsumfield.getText()).replace(',', '.'));
		} catch (NumberFormatException e) {

		}

		try {
			taxvalueParsed = Double.valueOf(this.invoicetaxvaluefield.getText().replace(',', '.'));
		} catch (NumberFormatException e) {

		}

		try {
			sumParsed = Double.valueOf(this.suminvoicefield.getText().replace(',', '.'));
		} catch (NumberFormatException e) {

		}

		loadData.addNewInvoice(INVOICEID, CUSTOMERID, false, this.taxnumberinvoicefield.getText(),
				this.streetinvoicefield.getText(), areaCodeParsed, this.cityinvoicefield.getText(),
				this.countyinvoicefield.getText(),
				this.countryinvoicecombobox.getSelectionModel().getSelectedItem().toString(), (double) (netsumParsed),
				taxpct, (double) (taxvalueParsed), (double) (sumParsed),
				this.currencyinvoicecombobox.getSelectionModel().getSelectedItem().toString(),
				this.noteinvoicefield.getText());
		MainWindowController.reloadUnpaidInvoices();
	}

	/**
	 * Part of the invoice creation process. Must run after "createInvoiceDatabase".
	 * Create and save invoice PDF.
	 */
	public void createInvoicePdf() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		this.createdInvoicePDFPath = createInvoicePdf(INVOICEID, sdf.format(AppDataSettings.todayDateFactory.get()),
				CUSTOMERID, false, this.taxnumberinvoicefield.getText(), this.customerCompanyname,
				this.customerForeSurname, this.streetinvoicefield.getText(), areaCodeParsed,
				this.cityinvoicefield.getText(), this.countyinvoicefield.getText(),
				this.countryinvoicecombobox.getSelectionModel().getSelectedItem().toString(), (double) (netsumParsed),
				taxpct, (double) (taxvalueParsed), (double) (sumParsed),
				this.currencyinvoicecombobox.getSelectionModel().getSelectedItem().toString(),
				this.noteinvoicefield.getText());
		System.out.println("Invoice created at: " + createdInvoicePDFPath);
		addNewInvoiceWindow.close();
		// Clear invoiceItemsData always after creation of the pdf:
		invoiceItemsData.clear();
	}

	/**
	 * Part of the invoice creation process. Must run after "createInvoicePdf".
	 * Start runnable that sends the invoice PDF as an email.
	 */
	public void createInvoiceSendEmail() {
		Locale locale = Locale.getDefault();
		invoiceCreationProcessEnded = true;
		Runnable sendEmailJob = new SendInvoiceEmail(INVOICEID, this.customerFirstname, this.customerLastname,
				this.selectedInvoiceCustomer.getEmail(), (double) (sumParsed),
				this.currencyinvoicecombobox.getSelectionModel().getSelectedItem().toString(), locale.getCountry(),
				createdInvoicePDFPath,
				AppDataSettings.languageBundle.getString("invoiceEmailSendTxt").replace("--%--", "" + INVOICEID));
		AppDataSettings.executeEmailJob.submit(sendEmailJob);

	}

	/**
	 * Task that starts the method createInvoice and with that the invoice creation
	 * process. Starts the invoice creation process and closes the create invoice
	 * window. Creates the invoice and saves it to the database, pdf and sends
	 * invoice PDF mail.
	 * 
	 * @return
	 */
	public Task invoiceCreationTask() {
		try {
			return new Task() {

				@Override
				protected Object call() throws Exception {
					updateMessage("...");
					progressLoading: while (true) {
						for (int i = 1; i < 100; i++) {
							updateProgress(i += 1, 100);
							Thread.sleep(100);
							if (NewInvoiceController.invoiceCreationProcessEnded) {
								break progressLoading;
							}
						}
					}

					return true;
				}

			};
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Opens InvoicePos Window
	 */
	@FXML
	public void handleAddNewInvoiceItem(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/NewInvoicePos.fxml"));
			Stage invoicePosWindow = new Stage();
			Scene scene = new Scene(root);
			invoicePosWindow.setScene(scene);
			invoicePosWindow.setResizable(false);
			invoicePosWindow.setTitle(AppDataSettings.languageBundle.getString("invoicePosWindowHeader"));
			NewInvoicePosController.invoiceId = INVOICEID;

			invoicePosWindow.show();

			NewInvoicePosController.invoicePosWindow = invoicePosWindow;

		} catch (IOException e) {

		}
	}

	private <S, T> TableColumn<S, T> buildTableColumn(String name, Function<S, Property<T>> property) {
		TableColumn<S, T> column = new TableColumn<>(name);
		column.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
		column.setOnEditCommit(edit -> {
			S rowValue = edit.getRowValue();
			property.apply(rowValue).setValue(edit.getNewValue());
		});
		return column;
	}

	/**
	 * Calculate net sum, sum and taxes sum for all invoice items of the open
	 * invoice
	 */
	public void calculateInvoiceSumsAll() {
		Double sum = loadData.calculateSumPosItems(INVOICEID, this.taxpct);
		suminvoicefield.setText("" + sum);
		int netsumCent = 0;
		for (InvoicePos item : invoiceItemsData) {
			netsumCent += ((item.getSumprice() * 100));
		}
		this.invoicenetsumfield.setText(String.format("%.2f", (double) (((double) netsumCent) / 100)));

		if (this.taxpct > 0.0) {
			this.invoicetaxvaluefield
					.setText(String.format("%.2f", (double) (((((double) netsumCent) * (this.taxpct / 100))) / 100)));
			this.suminvoicefield.setText(String.format("%.2f",
					(double) (((((double) netsumCent) * ((this.taxpct / 100)) + netsumCent)) / 100)));
		} else {
			this.suminvoicefield.setText(String.format("%.2f", (double) (((double) netsumCent) / 100)));

		}

	}

	/**
	 * Recalculate net sum, sum and taxes sum for all invoice items of the open
	 * invoice according to the new tax percentage
	 */

	public void refreshInvoiceSumsAllForTaxPct(String newValue) {

		if (newValue != null && !newValue.equals("")) {
			try {
				this.taxpct = Double.parseDouble((newValue).replace(',', '.'));
				double netsum = Double.parseDouble(this.invoicenetsumfield.getText().replace(',', '.'));
				int netsumCent = (int) (netsum * 100);

				if (this.taxpct > 0.0) {
					this.invoicetaxvaluefield.setText(
							"" + String.format("%.2f", (double) ((((double) netsumCent) * (this.taxpct / 100)) / 100)));
				}
				this.suminvoicefield.setText(String.format("%.2f",
						(double) ((((((double) netsumCent) * (this.taxpct / 100)) + ((double) netsumCent))) / 100)));

			} catch (NumberFormatException e) {
				System.out.println("Exception: " + e);

			}

		}
	}

	/**
	 * Create a pdf for an invoice in the package folder invoices
	 * 
	 * @return Returns the file path to the created invoice. If the invoice could
	 *         not be created, then null will be returned.
	 */
	public String createInvoicePdf(Integer id, String date, Integer customerid, boolean paid, String taxnumber,
			String customerCompanyname, String customerForenamesurname, String customerStreet, int customerZipcode,
			String customerCity, String customerCounty, String customerCountry, Double netsum, Double taxpct,
			Double taxval, Double sum, String currencyiso, String note) {

		Document document = new Document();
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream(AppDataSettings.INVOICESFILELOCATION + "/invoice_no" + id + ".pdf"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPTable emptyLine = new PdfPTable(1);
		emptyLine.setWidthPercentage(100);
		emptyLine.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		emptyLine.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		emptyLine.getDefaultCell().setFixedHeight(30);
		emptyLine.addCell("  ");

		// Invoice header
		PdfPTable invoiceHeader = new PdfPTable(3);
		invoiceHeader.setWidthPercentage(100);
		invoiceHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		invoiceHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		invoiceHeader.getDefaultCell().setFixedHeight(19);
		Image companyLogo = null;
		try {
			companyLogo = Image.getInstance(loadData.getAppsettings().get("invoiceCompanylogopath"));
		} catch (BadElementException e1) {
			ErrorReport.reportException(e1);
			System.out.println("" + e1);
			companyLogo = null;
		} catch (MalformedURLException e1) {
			ErrorReport.reportException(e1);
			System.out.println("" + e1);
			companyLogo = null;
		} catch (IOException e1) {
			ErrorReport.reportException(e1);
			System.out.println("" + e1);
			companyLogo = null;
		}
		if (companyLogo != null) {
			invoiceHeader.addCell(companyLogo);
		} else {
			invoiceHeader.addCell(new Phrase(new Chunk(" ")));
		}
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanystreet")).trim());
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanyemail")).trim());
		invoiceHeader.addCell(new Phrase(new Chunk((loadData.getAppsettings().get("invoiceCompanyname").trim()))));
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanypostcode") + " "
				+ loadData.getAppsettings().get("invoiceCompanycity")).trim());
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanytelephone")).trim());
		invoiceHeader.addCell(new Phrase(new Chunk((loadData.getAppsettings().get("invoiceCompanytaxno").trim()))));
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanycounty")).trim());
		invoiceHeader.addCell(loadData.getAppsettings().get("invoiceCompanywebsite"));
		invoiceHeader.addCell(new Phrase(new Chunk(" ")));
		invoiceHeader.addCell((loadData.getAppsettings().get("invoiceCompanycountry")).trim());
		invoiceHeader.addCell(" ");

		// Invoice customer address field - invoice date and invoice overview
		PdfPTable invoiceCustomer = new PdfPTable(2);
		invoiceCustomer.setWidthPercentage(100);
		invoiceCustomer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		invoiceCustomer.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		invoiceCustomer.getDefaultCell().setFixedHeight(19);
		invoiceCustomer.addCell((customerCompanyname).trim());
		invoiceCustomer.addCell(" ");
		invoiceCustomer.addCell(new Phrase(new Chunk((customerForenamesurname).trim())));
		invoiceCustomer.addCell(" ");
		invoiceCustomer.addCell(new Phrase(new Chunk((customerStreet).trim())));
		invoiceCustomer.addCell(" ");
		invoiceCustomer.addCell(new Phrase(new Chunk((customerZipcode + " " + customerCity).trim())));
		invoiceCustomer.addCell(" ");
		invoiceCustomer.addCell(new Phrase(new Chunk((customerCounty).trim())));
		invoiceCustomer
				.addCell((AppDataSettings.languageBundle.getString("invoiceColumnInvoiceDate") + ": " + date).trim());
		invoiceCustomer.addCell(new Phrase(new Chunk((customerCountry).trim())));
		invoiceCustomer.addCell((AppDataSettings.languageBundle.getString("invoiceTopayText")
				+ String.format(": %.2f ", sum) + currencyiso).trim());
		invoiceCustomer.addCell(new Phrase(new Chunk((taxnumber).trim())));
		invoiceCustomer.addCell(
				(AppDataSettings.languageBundle.getString("invoiceCustomeridText") + ": " + customerid).trim());

		// invoice heading
		PdfPTable invoiceHeading = new PdfPTable(1);
		invoiceHeading.setWidthPercentage(100);
		invoiceHeading.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		invoiceHeading.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		invoiceHeading.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		invoiceHeading.getDefaultCell().setFixedHeight(40);
		invoiceHeading.getDefaultCell().setPadding(10);
		invoiceHeading.addCell(AppDataSettings.languageBundle.getString("invoiceColumnInvoiceNo") + ": " + id);

		// Invoice footer (centered)
		PdfPTable invoiceFooter = new PdfPTable(1);
		invoiceFooter.setWidthPercentage(100);
		invoiceFooter.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		invoiceFooter.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		invoiceFooter.getDefaultCell().setFixedHeight(30);
		invoiceFooter.addCell(note);
		invoiceFooter.addCell(" ");

		// invoice last line footer for sub text
		PdfPTable invoiceEnd = new PdfPTable(1);
		invoiceEnd.setWidthPercentage(100);
		invoiceEnd.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		invoiceEnd.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		invoiceEnd.getDefaultCell().setFixedHeight(30);
		invoiceEnd.addCell(loadData.getAppsettings().get("invoiceCompanyadditionalinfo"));

		try {
			document.open();
			document.add(invoiceHeader);
			document.add(emptyLine);
			document.add(emptyLine);
			document.add(invoiceCustomer);
			document.add(emptyLine);
			document.add(emptyLine);
			document.add(invoiceHeading);
			document.add(emptyLine);
			document.add(emptyLine);
			document.add(createInvoicePdfTable(id, netsum, taxpct, taxval, sum, currencyiso));
			document.add(emptyLine);
			document.add(invoiceFooter);
			document.add(invoiceEnd);

		} catch (DocumentException e) {
			e.printStackTrace();
			document.close();
			return null;
		}
		document.close();
		// return (new File(".").getCanonicalPath()) + "invoices/invoice_no" + id +
		// ".pdf";
		return "invoices/invoice_no" + id + ".pdf";

	}

	/**
	 * Table for createInvoicePdf
	 * 
	 * @return PdfPTable the created table object for createInvoicePdf
	 */

	public PdfPTable createInvoicePdfTable(Integer id, Double netsum, Double taxpct, Double taxval, Double sum,
			String currencyiso) {
		PdfPTable invoiceTable = new PdfPTable(6);
		// Table header ***
		Stream.of(AppDataSettings.languageBundle.getString("invoiceItemColumnId"),
				AppDataSettings.languageBundle.getString("invoiceItemColumnName"),
				AppDataSettings.languageBundle.getString("invoiceItemColumnUnit"),
				AppDataSettings.languageBundle.getString("invoiceItemColumnPriceperunit"),
				AppDataSettings.languageBundle.getString("invoiceItemColumnSum"), "").forEach(columnTitle -> {
					PdfPCell header = new PdfPCell();
					header.setBackgroundColor(BaseColor.YELLOW);
					header.setBorderWidth(2);
					header.setPhrase(new Phrase(columnTitle));
					invoiceTable.addCell(header);
				});
		// Table content ***
		for (InvoicePos item : invoiceItemsData) {
			invoiceTable.addCell(item.getId() + "");
			invoiceTable.addCell(item.getItemname());
			invoiceTable.addCell(item.getUnit() + "");
			invoiceTable.addCell(item.getPriceperunit() + "");
			invoiceTable.addCell(item.getSumprice() + "");
			invoiceTable.addCell(currencyiso + " ");
		}
		// Table footer
		Stream.of(AppDataSettings.languageBundle.getString("invoiceColumnNetsum") + String.format(": %.2f  ", netsum),
				AppDataSettings.languageBundle.getString("invoiceColumnTaxpct") + String.format(": %.2f   ", taxpct),
				AppDataSettings.languageBundle.getString("invoiceColumnTaxvalue") + String.format(": %.2f   ", taxval),
				" = ", AppDataSettings.languageBundle.getString("invoiceColumnSum").toUpperCase()
						+ String.format(": %.2f ", sum),
				currencyiso + " ").forEach(columnTitle -> {
					PdfPCell footer = new PdfPCell();
					footer.setBackgroundColor(BaseColor.YELLOW);
					footer.setBorderWidth(0);
					footer.setPadding(5);
					footer.setPhrase(new Phrase(columnTitle));
					invoiceTable.addCell(footer);
				});

		return invoiceTable;
	}
}
