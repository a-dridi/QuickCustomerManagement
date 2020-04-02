package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import QuickCustomerManagment.AppDataSettings;

class AppDataSettingsTest {

	public static AppDataSettings ads = new AppDataSettings();
	
	/**
	 * Create database if its no existing;
	 */
	@BeforeAll
	public static void prepareData() {
		ads.loadDatabase();
	}
	
	/**
	 * Non existing invoice number. Should return invoice item id 1.
	 * @param invoiceid
	*/
	@Test
	void testLoadNewInvoicePosId() {
		System.out.println("test ok");
		int actual = ads.loadNewInvoicePosId(1402);
		int expected = 1;
		assertEquals(expected, actual);
	}
	
	
	
	/**
	 * Sum must be calculated from the invoice items of an invoice. Regardless which sum is written in the invoice table. 
	 */
	@Test
	void testCalculateSumPosItems() {

		ads.addNewInvoice(1, 1, true, "AT-32332322", "Teststreet", 4934, "The City", "Awesomeland", "Awesomestate", 14., 10., 10., 24., "EUR", "");
		ads.addNewInvoice(2, 1, true, "AT-36577222", "Lakestreet", 4100, "The Village", "Cooland", "Mystate", 10., 20., 1., 11., "USD", "");

		ads.addNewInvoicePosItem(1, "Milk", 2., 10., 20., 1);
		ads.addNewInvoicePosItem(1, "Cookies", 2., 5., 10., 1);

	    double actual = ads.calculateSumPosItems(1, 0.);
	    double expected = 30;
	    assertEquals(expected, actual);
	}
	
	/**
	 * Change setting: default currency value to a real value
	 */
	@Test
	void testUpdateSetting1() {
		boolean actual = ads.updateSetting("defaultCurrencyIso", "EURO");
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
	/**
	 * Change non existing setting
	 */
	@Test
	void testUpdateSetting2() {
		boolean actual = ads.updateSetting("anawesomesetting", "OKAY");
		boolean expected = false;
		assertEquals(expected, actual);
	}
	
	@AfterAll
	public static void closeData() {
		//ads.deleteDatabase();
	}

	
	

}
