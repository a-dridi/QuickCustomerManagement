package QuickCustomerManagment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.sqlite.SQLiteException;

import javafx.scene.control.Alert;
import model.Customer;
import model.Invoice;
import model.UnpaidInvoice;

/**
 * Saves and loads Data (such as Currencies) and Settings that are needed for
 * the application and startup of the application.
 *
 * @author
 */
public class AppDataSettings implements Serializable {
	private String errorMessage;
	public static ResourceBundle languageBundle;
	public static Supplier <Date> todayDateFactory = Date::new;
	private Map<String, String> currencies = new TreeMap<>();
	private List<String> countries = new ArrayList<>();
	private Map<String, String> appsettings = new TreeMap<>();

	public static String INVOICESFILELOCATION = "invoices";

	/**
	 * Load currencies and countries from a file from a github repos. These are
	 * saved in the static class variables currencies (a Map) and countries (a
	 * List).
	 * 
	 * @return
	 */
	public boolean loadCurrenciesCountries() {
		// Fetch and load currencies and country names
		HttpClient client = HttpClient.newHttpClient();
		String urlJsonFile = "https://raw.githubusercontent.com/lorey/list-of-countries/master/csv/countries.csv";
		try {
			Path downloadPath = Paths.get("countries.csv");
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlJsonFile))
					.header("Accept", "application/csv").build();
			HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(downloadPath));
			// Get saved json file
			// Path filePath = response.body();

			BufferedReader csvReader = new BufferedReader(new FileReader(response.body().toFile()));
			String line = null;
			String[] lineArray;
			currencies.put("USD", "US. Dollar");
			currencies.put("EUR", "Euro");
			currencies.put("GBP", "British Pound");
			currencies.put("CHF", "Swiss Frank");
			currencies.put("JPY", "Japanese Yen");
			currencies.put("CNY", "Chinese Yuan");

			// header
			line = csvReader.readLine();

			do {
				line = csvReader.readLine();
				// Debug:
				// System.out.println(line);
				if (line != null) {
					lineArray = line.split(";");
					currencies.put(lineArray[5], lineArray[6]);
					countries.add(lineArray[11]);
				}
			} while (line != null);

			Collections.sort(countries, new SortCountries());

		} catch (IOException | InterruptedException e) {
			System.out.println("-> " + e);
			return false;
		}

		return true;

	}

	/**
	 * Create (if it does not exist) database and load app settings
	 * 
	 * @return Database was loaded successful
	 */
	public boolean loadDatabase() {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			// c.setAutoCommit(false);

			// Create database if it was not created;
			boolean databaseFirstTime = false;
			Statement st = c.createStatement();
			String sql = "SELECT * from appsettings";
			ResultSet rs;
			try {
				rs = st.executeQuery(sql);
				if (rs.next() && rs.getString(1) != null && !rs.getString(1).equals("")) {
				} else {
					System.out.println("Database loaded for the first time");
					databaseFirstTime = true;
				}
				rs.close();
			} catch (SQLiteException e) {
				System.out.println("Database loaded for the first time");
				databaseFirstTime = true;
			}
			st.close();
			if (databaseFirstTime) {

				st = c.createStatement();
				sql = "CREATE TABLE IF NOT EXISTS appsettings " + "(description TEXT PRIMARY KEY," + "value TEXT)";
				st.execute(sql);
				st.close();
				st = c.createStatement();
				sql = "CREATE TABLE IF NOT EXISTS customer " + "(id INTEGER PRIMARY KEY, " + "email TEXT,"
						+ "forename TEXT," + "lastname TEXT," + "companyname TEXT," + "taxnumber TEXT," + "street TEXT,"
						+ "zipcode INTEGER," + "city TEXT," + "county TEXT," + "country TEXT," + "currencyiso TEXT,"
						+ "note TEXT" + ")";
				st.execute(sql);
				st.close();
				st = c.createStatement();
				// invoice netsum, taxval and sum are in cent
				sql = "CREATE TABLE IF NOT EXISTS invoice " + "(id INTEGER PRIMARY KEY, " + "date TEXT NOT NULL, "
						+ "customerid INT NOT NULL," + "paid NUMERIC NOT NULL," + "street TEXT," + "areacode INT,"
						+ "city TEXT," + "county TEXT," + "country TEXT," + "netsum REAL," + "taxpct REAL,"
						+ "taxval REAL," + "sum REAL," + "currencyiso TEXT," + "note TEXT,"
						+ "FOREIGN KEY (customerid) REFERENCES customer (id)" + ")";
				st.execute(sql);
				st.close();
				st = c.createStatement();
				// invoicepos priceperunit and sumprice are in cent
				sql = "CREATE TABLE IF NOT EXISTS invoicepos " + "(globalid INTEGER PRIMARY KEY, id INT NOT NULL, "
						+ "itemname TEXT NOT NULL," + "unit REAL, " + "priceperunit REAL,"
						+ "sumprice REAL, invoiceid INT," + "FOREIGN KEY (invoiceid) REFERENCES invoice (id)" + ")";
				st.execute(sql);
				st.close();
				st = c.createStatement();
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "defaultCurrencyIso" + "', '" + "USD"
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailForenameSurname" + "', '" + " " + "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailEmailaddress" + "', '" + "" + "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailHost" + "', '" + "" + "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailPort" + "', " + "465" + ")";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailUsername" + "', '" + ""
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "emailPassword" + "', '" + ""
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "settingsNote" + "', '" + "..." + "')";
				st.execute(sql);
				// invoiceCompanyxxx - Company information that is displayed on invoices
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanyname" + "', '" + "."
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanystreet" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanypostcode" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanycity" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanycounty" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanycountry" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanytaxno" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanyemail" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanytelephone" + "', '"
						+ " " + "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanywebsite" + "', '" + " "
						+ "')";
				st.execute(sql);
				sql = "INSERT INTO appsettings (description, value) VALUES ('" + "invoiceCompanyadditionalinfo" + "', '"
						+ " " + "')";
				st.execute(sql);

				st.close();
			}
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("errorWindowHeader").toUpperCase());
			alert.setContentText(AppDataSettings.languageBundle.getString("errordatabase"));
			alert.show();
			return false;
		} finally {
			try {
				c.close();
				System.out.println("Database loading end.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * App settings that are used in this app and displayed in Settings Window
	 * 
	 * @throws SQLException
	 */
	public void loadAppSettings() {

		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery("SELECT description, value FROM appsettings");

			while (rs != null && rs.next()) {
				this.appsettings.put(rs.getString("description"), rs.getString("value"));
			}
			rs.close();
			st.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "loadAppSettings: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Deletes physical database file
	 */
	public void deleteDatabase() {
		File file = new File("customermanagment.db");
		file.delete();
	}

	/**
	 * Save new Customer
	 * 
	 * @return
	 */

	public boolean addNewCustomer(String customerEmail, String forenameclientfield, String surnameclientfield, String companynameclientfield,
			String taxnumber, String streetclientfield, Integer zipcodeclientfield, String cityclientfield,
			String countyclientfield, String countryclientcombobox, String currencyclientcombobox,
			String noteclienttextarea) {

		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");

			// c.setAutoCommit(false);
			Statement st = c.createStatement();
			String sql = "INSERT INTO Customer (email, forename, lastname, companyname, taxnumber, street, zipcode, city, county, country, currencyiso, note) VALUES ('"
					+ customerEmail+ "', '" + forenameclientfield + "', '" + surnameclientfield + "', '" + companynameclientfield + "', '"
					+ taxnumber + "', '" + streetclientfield + "', " + zipcodeclientfield + ", '" + cityclientfield
					+ "', '" + countyclientfield + "', '" + countryclientcombobox + "', '" + currencyclientcombobox
					+ "', '" + noteclienttextarea + "')";
			st.executeUpdate(sql);
			st.close();
			// c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "add New Customer Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Add new invoice item into an invoice. Converts automatically in cent
	 * amount/values when needed.
	 * 
	 * @param priceperunit Amount will be saved in cent in the database
	 * @param sumprice     Amount will be saved in cent in the database
	 */
	public boolean addNewInvoicePosItem(Integer id, String itemname, Double unit, Double priceperunit, Double sumprice,
			Integer invoiceId) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();
			int priceperunitCent = (int) ((priceperunit * 100));
			int sumpriceCent = (int) ((sumprice * 100));

			String sql = "INSERT INTO Invoicepos (id, itemname, unit, priceperunit, sumprice, invoiceid) VALUES (" + id
					+ ", '" + itemname + "', " + unit + ", " + priceperunitCent + ", " + sumpriceCent + ", " + invoiceId + ")";
			st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "addNewInvoicePosItem Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Delete invoice item into an invoice
	 */
	public boolean deleteInvoicePosItem(Integer id,
			Integer invoiceId) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();

			String sql = "DELETE FROM invoicepos WHERE id = " + id + " AND invoiceid = " + invoiceId;
			st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "deleteInvoicePosItem Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Add new invoice with the current date and time Netsum, taxvalue and sum are
	 * saved in cent integer
	 */
	public boolean addNewInvoice(Integer id, Integer customerid, boolean paid, String taxnumber, String street,
			int areacode, String city, String county, String country, Double netsum, Double taxpct, Double taxval,
			Double sum, String currencyiso, String note) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();

			int netsumCent = (int) ((netsum * 100));
			int taxvalueCent = (int) ((taxval * 100));
			int sumCent = (int) ((sum * 100));

			int paidValue = paid ? 1 : 0;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

			String sql = "INSERT INTO invoice (id, date, customerid, paid, street, areacode, city, county, country, netsum, taxpct, taxval, sum, currencyiso, note) VALUES ("
					+ id + ", '" + sdf.format(AppDataSettings.todayDateFactory.get()) + "', " + customerid + ", " + paidValue + ", '" + street + "', "
					+ areacode + ", '" + city + "', '" + county + "', '" + country + "', " + netsumCent + ", " + taxpct
					+ ", " + taxvalueCent + ", " + sumCent + ", '" + currencyiso + "', '" + note + "')";
			st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "add New Invoice Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

	}

	/**
	 * Delete invoice
	 */
	public boolean deleteInvoice(Integer id) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();

			String sql = "DELETE FROM invoice WHERE id = " + id;

			st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "deleteInvoice Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

	}

	/**
	 * Load UI text and descriptions for a selected language
	 * 
	 * @param languageCode ISO code for language (ex. en for English)
	 */
	public void loadUIText(String languageCode) {
		if (languageCode != null && languageCode != "") {
			Locale locale = new Locale(languageCode);
			languageBundle = ResourceBundle.getBundle("languages.lang", locale);
		} else {
			loadUIText("en");
		}
	}

	public int loadNewInvoiceId() {
		Connection c = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			int id = 0;
			Statement st = c.createStatement();
			String sql = "SELECT id from invoice ORDER BY id desc LIMIT 1";
			ResultSet rs = st.executeQuery(sql);

			if (rs != null && rs.next()) {
				id = rs.getInt(1);
				id += 1;
			} else {
				id += 1;
			}
			rs.close();
			st.close();
			c.commit();
			c.close();
			return id;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "loadNewInvoiceId: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		}
	}

	/**
	 * Returns a new invoice pos (invoice item) id that should be used
	 * 
	 * @param invoiceid Id of the invoice that has this invoice item id
	 */
	public int loadNewInvoicePosId(int invoiceid) {

		Connection c = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			int id = 0;
			Statement st = c.createStatement();
			String sql = "SELECT id from invoicepos WHERE invoiceid = " + invoiceid + " ORDER BY id desc LIMIT 1";
			ResultSet rs = st.executeQuery(sql);

			if (rs != null && rs.next()) {
				id = rs.getInt("id");
				id += 1;
			} else {
				id += 1;
			}
			rs.close();
			st.close();
			c.commit();
			c.close();
			return id;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "loadNewInvoicePosId: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		}

	}

	/**
	 * Calculates sum of a invoice id and returns the sum in currency value and not
	 * in cent.
	 * 
	 * @param taxpct Tax percentage for this invoice id
	 * @return sum incl. taxes
	 */
	public double calculateSumPosItems(Integer invoiceId, Double taxpct) {
		AppDataSettings data = new AppDataSettings();
		Double sum = data.calculateNetSumPosItems(invoiceId);
		if (sum > 0) {
			return (((sum * taxpct) / 100) + sum);
		} else {
			return 0;
		}
	}

	/**
	 * Calculate net sum for a invoice id in currency value and not in cent.
	 * 
	 * @param invoiceId
	 * @return net sum excl. taxes
	 */
	public double calculateNetSumPosItems(Integer invoiceId) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			int id = 0;
			Statement st = c.createStatement();
			String sql = "SELECT sum(sumprice) from invoicepos WHERE invoiceid = " + invoiceId;
			ResultSet rs = st.executeQuery(sql);
			Double sum = 0.0;
			if (rs != null && rs.next()) {
				sum = (double) ((rs.getInt(1)) / 100);
			}
			rs.close();
			st.close();
			c.close();
			return sum;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "calculateNetSumPosItems: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		}
	}

	/**
	 * Calculate taxes value of a invoice id in currency value and not in cent.
	 * 
	 * @param invoiceId
	 * @param taxpct    the tax percentage of an invoice id
	 * @return
	 */
	public double calculateTaxesSumPosItems(Integer invoiceId, Double taxpct) {
		AppDataSettings data = new AppDataSettings();
		Double sum = data.calculateNetSumPosItems(invoiceId);
		if (sum > 0) {
			return (((sum * taxpct) / 100));
		} else {
			return 0;
		}
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customerList = new ArrayList();
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			int id = 0;
			Statement st = c.createStatement();
			String sql = "SELECT id, email, forename, lastname, companyname, taxnumber, street, zipcode, city, county, country, currencyiso, note FROM customer";
			ResultSet rs = st.executeQuery(sql);
			Double sum = 0.0;

			while (rs != null && rs.next()) {
				customerList.add(new Customer(rs.getInt("id"), rs.getString("email"), rs.getString("forename"),
						rs.getString("lastname"), rs.getString("companyname"), rs.getString("taxnumber"),
						rs.getString("street"), rs.getInt("zipcode"), rs.getString("city"), rs.getString("county"),
						rs.getString("country"), rs.getString("currencyiso"), rs.getString("note")));
			}
			rs.close();
			st.close();
			c.close();

			return customerList;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "calculateNetSumPosItems: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	public List<UnpaidInvoice> getUnpaidInvoices() {
		List<UnpaidInvoice> unpaidInvoices = new ArrayList<>();
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();
			String sql = "SELECT invoice.id, invoice.date, invoice.sum, invoice.currencyiso, customer.companyname, customer.forename, customer.lastname FROM invoice INNER JOIN customer ON invoice.customerid=customer.id WHERE paid=0";
			ResultSet rs = st.executeQuery(sql);

			while (rs != null && rs.next()) {
				unpaidInvoices.add(new UnpaidInvoice(rs.getInt(1), rs.getString(2), rs.getDouble(3)/100, rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7),
						AppDataSettings.languageBundle.getString("unpaidinvoicesPaidButton"),AppDataSettings.languageBundle.getString("unpaidinvoicesShowinvoiceButton")));
			}
			rs.close();
			st.close();
			c.close();
			return unpaidInvoices;

		} catch (Exception e) {
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	/**
	 * Change state of an invoice to paid (1).
	 * 
	 * @return
	 */
	public boolean payInvoice(Integer invoiceId) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();

			String sql = "UPDATE invoice SET paid=1 WHERE id = " + invoiceId;

			st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();

			return true;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "deleteInvoice Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

	}

	/**
	 * Update a setting of this app that is saved in the database
	 * 
	 * @return
	 */
	public boolean updateSetting(String settingDescriptionKey, String newSettingsValue) {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:customermanagment.db");
			c.setAutoCommit(false);
			Statement st = c.createStatement();

			String sql = "UPDATE appsettings SET value='" + newSettingsValue + "' WHERE description = '"
					+ settingDescriptionKey + "'";

			int ret = st.executeUpdate(sql);
			st.close();
			c.commit();
			c.close();
			if (ret > 0) {
				// Setting updated
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			errorMessage = "updateSetting Error: " + e.getMessage();
			try {
				c.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	public Map<String, String> getAppsettings() {
		return appsettings;
	}

	public void setAppsettings(Map<String, String> appsettings) {
		this.appsettings = appsettings;
	}

	public Map<String, String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, String> currencies) {
		this.currencies = currencies;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static ResourceBundle getLanguageBundle() {
		return languageBundle;
	}

	public static void setLanguageBundle(ResourceBundle languageBundle) {
		AppDataSettings.languageBundle = languageBundle;
	}

}