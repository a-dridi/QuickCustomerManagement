package QuickCustomerManagment;

import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javafx.scene.control.Alert;

/**
 * Send Invoice Email in background through threads
 *
 */
public class SendInvoiceEmail implements Runnable {

	private AppDataSettings loadData = new AppDataSettings();
	private int invoiceId;
	private String customerForename;
	private String customerSurname;
	private String customerEmail;
	private double invoiceamount;
	private String currencyiso;
	private String languageCode;
	private String relativeInvoiceFilePath;

	public SendInvoiceEmail(int invoiceId, String customerForename, String customerSurname, String customerEmail,
			double invoiceamount, String currencyiso, String languageCode, String relativeInvoiceFilePath) {
		super();
		this.invoiceId = invoiceId;
		this.customerForename = customerForename;
		this.customerSurname = customerSurname;
		this.customerEmail = customerEmail;
		this.invoiceamount = invoiceamount;
		this.currencyiso = currencyiso;
		this.languageCode = languageCode;
		this.relativeInvoiceFilePath = relativeInvoiceFilePath;
	}

	@Override
	public void run() {
		this.loadData.loadAppSettings();

		System.out.println("Send invoice to: " + customerEmail);
		String emailStatus = sendInvoiceEmail(invoiceId, customerForename, customerSurname, customerEmail,
				invoiceamount, currencyiso, languageCode, relativeInvoiceFilePath);

		if (emailStatus.isEmpty()) {
			System.out.println("Invoice email was sent. ");
		} else {
			System.out.println("Invoice could NOT be sent. ");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(AppDataSettings.languageBundle.getString("emailsendingerrorWindowHeader").toUpperCase());
			alert.setContentText(emailStatus);
			alert.show();

		}

	}

	/**
	 * Send Invoice as an email with the defined settings in the table appsettings
	 * (MailSettingsController)
	 * 
	 * @return empty string, if email could be sent successfully. Otherwise the
	 *         error message.
	 */
	public String sendInvoiceEmail(int invoiceId, String customerForename, String customerSurname, String customerEmail,
			double invoiceamount, String currencyiso, String languageCode, String relativeInvoiceFilePath) {
		Optional<String> attachmentFilePath = Optional.ofNullable(relativeInvoiceFilePath);
		if (attachmentFilePath.isEmpty() || attachmentFilePath.get().length() < 5) {
			return "Invoice pdf could not be created. Please check your invoices folder.";
		}

		String emailSender = this.loadData.getAppsettings().get("emailEmailaddress");
		String emailHostString = this.loadData.getAppsettings().get("emailHost");
		String emailPortString = this.loadData.getAppsettings().get("emailPort");
		InvoiceMailAuthenticator authentication = new InvoiceMailAuthenticator();

		try {
			if (emailHostString.isEmpty() || authentication.getPasswordAuthentication() == null
					|| customerEmail.isEmpty()) {
				return "Please configure and enter all the email settings in the email configuration.";
			}
		} catch (NullPointerException e) {
			Logger.getLogger(NewInvoiceController.class.getName()).log(Level.SEVERE, null, e);
			return "Please configure and enter all the email settings in the email configuration.";
		}

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", emailHostString);
		try {
			Integer portParsed = Integer.valueOf(emailPortString);
			if (portParsed == 465) {
				properties.setProperty("mail.smtp.port", emailPortString);
				properties.setProperty("mail.smtp.auth", "true");
				properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			} else if (portParsed == 587) {
				properties.setProperty("mail.smtp.starttls.enable", "true");
				properties.setProperty("mail.smtp.auth", "true");

			} else if (portParsed == 25) {
				properties.setProperty("mail.smtp.port", emailPortString);
				properties.setProperty("mail.smtp.auth", "true");

			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			properties.setProperty("mail.smtp.starttls.enable", "false");
			properties.setProperty("mail.smtp.port", "25");

		}
		Lock lock = new ReentrantLock();
		Session session = null;
		try {
			lock.lockInterruptibly();
		    session = Session.getDefaultInstance(properties, authentication);
		} catch (Exception e) {
			Logger.getLogger(NewInvoiceController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			lock.unlock();
		}

		try {
			Message invoiceMessage = new MimeMessage(session);
			// invoiceMessage.setFrom(new InternetAddress(emailUsernameString + "@" +
			// emailHostString));
			if (this.loadData.getAppsettings().get("invoiceCompanyname").equals("")
					|| this.loadData.getAppsettings().get("invoiceCompanyname").equals(".")) {
				invoiceMessage.setFrom(new InternetAddress(emailSender));
			} else {
				invoiceMessage.setFrom(
						new InternetAddress(emailSender, this.loadData.getAppsettings().get("invoiceCompanyname")));
			}
			invoiceMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(customerEmail));
			invoiceMessage.setSubject(AppDataSettings.languageBundle.getString("invoicemailNewInvoiceSubjectText")
					.replace("--%--", "" + invoiceId));
			Multipart messageParts = new MimeMultipart();
			// Body email
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(AppDataSettings.languageBundle.getString("invoicemailEmailtextgreetingText")
					.replace("--%--", customerForename + " " + customerSurname)
					+ "\n\n"
					+ AppDataSettings.languageBundle.getString("invoicemailEmailtextcontentText")
							.replace("--%1--", "" + invoiceId)
							.replace("--%2--", String.format("%.2f " + currencyiso, invoiceamount, Locale.US))
					+ "\n\n" + AppDataSettings.languageBundle.getString("invoicemailEmailtextendgreetingsText") + "\n"
					+ this.loadData.getAppsettings().get("emailForenameSurname") + "\n\n"
					+ this.loadData.getAppsettings().get("invoiceCompanyname") + "\n"
					+ this.loadData.getAppsettings().get("invoiceCompanystreet") + "\n"
					+ this.loadData.getAppsettings().get("invoiceCompanypostcode").trim() + " "
					+ this.loadData.getAppsettings().get("invoiceCompanycity") + "\n"
					+ this.loadData.getAppsettings().get("invoiceCompanycountry") + "\n\n"
					+ this.loadData.getAppsettings().get("invoiceCompanytelephone") + "\n"
					+ this.loadData.getAppsettings().get("invoiceCompanyemail") + "\n"
					+ this.loadData.getAppsettings().get("invoiceCompanywebsite") + "\n");

			messageParts.addBodyPart(messageBodyPart);
			// Attachment email
			DataSource attachmentSource = new FileDataSource(attachmentFilePath.get());
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(attachmentSource));
			messageBodyPart.setFileName(
					AppDataSettings.languageBundle.getString("invoicefilenameHeaderText") + invoiceId + ".pdf");
			messageParts.addBodyPart(messageBodyPart);

			invoiceMessage.setContent(messageParts);
			Transport.send(invoiceMessage);
			return "";
		} catch (Exception e) {
			Logger.getLogger(NewInvoiceController.class.getName()).log(Level.SEVERE, null, e);
			return AppDataSettings.languageBundle.getString("invoicemailerrorEmailNotSentText") + ": " + e.getCause()
					+ " - " + e.getMessage();
		}
	}

}
