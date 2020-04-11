package model;

import QuickCustomerManagment.AppDataSettings;
import QuickCustomerManagment.MainWindowController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


/**
 * Unpaid Invoice Table with paid button implementation
 * 
 * @author A.Dridi
 *
 */
public class UnpaidInvoice {
	private Integer invoiceId;
	private String date;
	private Double sum;
	private String currencyIso;
	private String customercompanyname;
	private String customerforename;
	private String customersurname;
	private Button paid;
	private Button showinvoice;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCustomercompanyname() {
		return customercompanyname;
	}

	public void setCustomercompanyname(String customercompanyname) {
		this.customercompanyname = customercompanyname;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public String getCurrencyIso() {
		return currencyIso;
	}

	public void setCurrencyIso(String currencyIso) {
		this.currencyIso = currencyIso;
	}

	public String getCustomerforename() {
		return customerforename;
	}

	public void setCustomerforename(String customerforename) {
		this.customerforename = customerforename;
	}

	public String getCustomersurname() {
		return customersurname;
	}

	public void setCustomersurname(String customersurname) {
		this.customersurname = customersurname;
	}

	public Button getPaid() {
		return paid;
	}

	public void setPaid(Button paid) {
		this.paid = paid;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	public Button getShowinvoice() {
		return showinvoice;
	}

	public void setShowinvoice(Button showinvoice) {
		this.showinvoice = showinvoice;
	}

	public UnpaidInvoice(Integer invoiceId, String date, Double sum, String currencyIso, String customercompanyname,
			String customerforename, String customersurname, String paidButtonDescription, String showinvoiceButtonDescription) {
		AppDataSettings updateDatabase = new AppDataSettings();
		
		this.invoiceId = invoiceId;
		this.date = date;
		this.customercompanyname = customercompanyname;
		this.sum = sum;
		this.currencyIso = currencyIso;
		this.customerforename = customerforename;
		this.customersurname = customersurname;
		this.paid = new Button(paidButtonDescription);
		this.paid.setId(invoiceId + "");
		//Button id is also the invoice id, that will be used to perform update on the database;
		this.paid.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Button selectedButton = (Button) arg0.getSource();
				try {
				Integer clickedInvoiceIdParsed = Integer.parseInt(selectedButton.getId());
				if(updateDatabase.payInvoice(clickedInvoiceIdParsed)) {
					MainWindowController.unpaidInvoicesData.clear();
					MainWindowController.unpaidInvoicesData.addAll(updateDatabase.getUnpaidInvoices());
					System.out.println("Invoice id paid: " + selectedButton.getId());
					MainWindowController.reloadUnpaidInvoices();
				}
				} catch(NumberFormatException e) {
					System.out.println(""+e);
				}
			}

		});
		this.showinvoice = new Button(showinvoiceButtonDescription);
		this.showinvoice.setId(invoiceId+"");
		this.showinvoice.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Button selectedButton = (Button) arg0.getSource();
				try {
				Integer clickedInvoiceIdParsed = Integer.parseInt(selectedButton.getId());
				MainWindowController.showInvoice(clickedInvoiceIdParsed);
				} catch(NumberFormatException e) {
					System.out.println(""+e);
				}
			}
			
		});
	}

}
