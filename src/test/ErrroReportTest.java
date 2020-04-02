package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import QuickCustomerManagment.ErrorReport;

public class ErrroReportTest {

	
	@Test
	/**
	 * No nullpointer exception - Adding valid data to the Error Report (Errors)
	 */
	void testAddingNewErrorReportError1() {
		String actual= ErrorReport.reportError("NullPointerException", "A new nullpointerexception in the class NewCustomerController");
		assertEquals(true, actual!=null);
	}
	
	@Test
	/**
	 * Data added to the server - Adding valid data to the Error Report (Errors)
	 */
	void testAddingNewErrorReportError2() {
		String actual= ErrorReport.reportError("NullPointerException", "A new nullpointerexception in the class NewCustomerController");
		String expected = "[{\"Add\":\"New error was added successfully\"}]";
		System.out.println("Error Report Server output: " + actual);
		assertEquals(expected, actual);
	}
}
