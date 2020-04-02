package model;

import java.io.Serializable;

import QuickCustomerManagment.MainWindowController;
import QuickCustomerManagment.NewInvoiceController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The item of an invoice
 * 
 * @author
 *
 */
public class InvoicePos implements Serializable {

	private Integer id;
	private String itemname;
	private Double unit;
	// later converted into cent, when it is saved into the db:
	private double priceperunit;
	// later converted into cent, when it is saved into the db:
	private double sumprice;
	private Integer invoiceid;
	private Button deleteButton;

	public InvoicePos() {

	}

	public InvoicePos(Integer id, String itemname, Double unit, double priceperunit, double sumprice, Integer invoiceid) {
		super();
		this.id = id;
		this.itemname = itemname;
		this.unit = unit;
		this.priceperunit = priceperunit;
		this.sumprice = sumprice;
		this.invoiceid = invoiceid;
		
	}

	public InvoicePos(Integer id, String itemname, Double unit, double priceperunit, double sumprice) {
		super();
		this.id = id;
		this.itemname = itemname;
		this.unit = unit;
		this.priceperunit = priceperunit;
		this.sumprice = sumprice;
	

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public double getPriceperunit() {
		return priceperunit;
	}

	public void setPriceperunit(int priceperunit) {
		this.priceperunit = priceperunit;
	}

	public double getSumprice() {
		return sumprice;
	}

	public void setSumprice(int sumprice) {
		this.sumprice = sumprice;
	}

	public Integer getInvoiceid() {
		return invoiceid;
	}

	public void setInvoiceid(Integer invoiceid) {
		this.invoiceid = invoiceid;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public void setPriceperunit(double priceperunit) {
		this.priceperunit = priceperunit;
	}

	public void setSumprice(double sumprice) {
		this.sumprice = sumprice;
	}

	@Override
	public String toString() {
		return "InvoicePos [id=" + id + ", itemname=" + itemname + ", unit=" + unit + ", priceperunit=" + priceperunit
				+ ", sumprice=" + sumprice + ", invoiceid=" + invoiceid + "]";
	}

}
