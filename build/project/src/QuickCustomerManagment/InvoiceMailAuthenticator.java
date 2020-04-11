package QuickCustomerManagment;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class InvoiceMailAuthenticator extends Authenticator {

	private AppDataSettings loadData = new AppDataSettings();

	public InvoiceMailAuthenticator() {
		super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		loadData.loadAppSettings();

		String emailUsernameString = this.loadData.getAppsettings().get("emailUsername");
		String emailPasswordString = this.loadData.getAppsettings().get("emailPassword");

		try {
			if (emailUsernameString.isEmpty() || emailPasswordString.isEmpty()) {
				return null;
			}
		} catch (NullPointerException e) {
			Logger.getLogger(NewInvoiceController.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
		return new PasswordAuthentication(emailUsernameString, emailPasswordString);
	}
}
