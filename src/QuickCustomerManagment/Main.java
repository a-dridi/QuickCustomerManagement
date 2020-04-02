package QuickCustomerManagment;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import java.net.http.HttpClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage mainwindowStage;

	/**
	 * Start main window of application
	 * 
	 * @param stage main window of application. Also controlled through other
	 *              Controller classes.
	 * @throws Exception
	 */
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/QuickCustomerManagment/MainWindow.fxml"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add("/styles/Styles.css");

			stage.setTitle("Quick Customer Managment");
			stage.setScene(scene);
			stage.show();
			this.mainwindowStage = stage;

		} catch (Exception e) {
			e.printStackTrace();
			ErrorReport.reportException(e);
		}
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application. main()
	 * serves only as fallback in case the application can not be launched through
	 * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores
	 * main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
