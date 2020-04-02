package QuickCustomerManagment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Class that reports Error for quality assurance. No personal information is
 * submitted. Needs internet connection.
 * 
 * Requirement: Apache HttpClient 4.5.12(httpclient-4.5.12.jar), HttpCore 4.4.13
 * (httpcore-4.4.13.jar) and ReportServerUrl/ApplicationName/Username/Password file "credentials.txt".
 * Compatible with older Java versions. Can be used from Java 8+.
 * 
 * Link to the dependencies files: http://hc.apache.org/downloads.cgi
 *
 */
public class ErrorReport {

	// CHANGE THIS: full address of the HTTP API where errors can be add
	public static String ERRORREPORTURL = "";
	// CHANGE THIS: application name
	public static String APPLICATIONNAME = "";

	/**
	 * Report error to report server
	 * 
	 * @param errorTitle
	 * @param errorDescription
	 * @return the response from the report server. Null if error did occur during
	 *         the process. Empty string if server did not save the error
	 */
	public static String reportError(String errorTitle, String errorDescription) {
		// READ report url, application name, username and password credentials.
		String username = "";
		String password = "";
		try (BufferedReader readCredentials = new BufferedReader(new FileReader("credentials.txt"))) {
			try {
				ERRORREPORTURL = (readCredentials.readLine().split("="))[1];
				System.out.println(""+ERRORREPORTURL);
				APPLICATIONNAME = (readCredentials.readLine().split("="))[1];
				System.out.println(""+APPLICATIONNAME);
				username = (readCredentials.readLine().split("="))[1];
				password = (readCredentials.readLine().split("="))[1];
			} catch (NullPointerException e) {
				System.out.println(
						"FILE credentials.txt should have two lines with \"username=MYUSERNAME\" and \"password=MYPASSWORD\"");
			}
		} catch (IOException e) {
			System.out.println("FILE credentials.txt is MISSING in the root folder: " + e.getMessage());

			return null;
		}
		
		CredentialsProvider authProvider = new BasicCredentialsProvider();
		HttpPost addErrorReportRequest = new HttpPost(ERRORREPORTURL);

		// Error Report content
		String errorReportJSONContent = "{\"title\": \""
				+ errorTitle.replaceAll("\"", "'").replaceAll("\r", " - ").replaceAll("\n", " - ") + "\",";
		errorReportJSONContent += "\"application\": \"" + APPLICATIONNAME + "\",";
		if (errorDescription.length() > 400) {
			errorDescription = errorDescription.substring(0, 399);
		}
		errorReportJSONContent += "\"description\": \""
				+ errorDescription.replaceAll("\"", "'").replaceAll("\r", " - ").replaceAll("\n", " - ") + "\"}";

		// Create Report content as JSON file for the error report server
		StringEntity errorReportEntity = new StringEntity(errorReportJSONContent, ContentType.APPLICATION_JSON);
		addErrorReportRequest.setEntity(errorReportEntity);


		// Authentication for the error report server
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		authProvider.setCredentials(AuthScope.ANY, credentials);

		// Send error report
		try (CloseableHttpClient errorReportClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(authProvider).build();
				CloseableHttpResponse errorReportResponse = errorReportClient.execute(addErrorReportRequest)) {

			HttpEntity responseContent = errorReportResponse.getEntity();
			List<String> readResponse = new BufferedReader(new InputStreamReader(responseContent.getContent())).lines()
					.collect(Collectors.toList());
			return readResponse.toString();
		} catch (IOException e) {
			System.out.println("Error report could not be send to server: " + e);
			return "";
		}

	}

	/**
	 * Report exception error to report server
	 * 
	 * @return the response from the report server
	 */
	public static String reportException(Exception e) {
		StringBuilder errorMessage = new StringBuilder();

		try {
			errorMessage.append(e.getMessage());
			errorMessage.append(" - Class: ");
			errorMessage.append(e.getClass().toString());
		} catch (Exception exception) {
			errorMessage.append("-");
		}

		return reportError(e.getCause().toString(), errorMessage.toString());
	}
}
