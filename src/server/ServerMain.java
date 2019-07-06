package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server_window.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}