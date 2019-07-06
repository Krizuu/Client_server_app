module SuperServer {
    requires javafx.fxml;
    requires javafx.controls;
    requires jdk.scripting.nashorn;
    requires org.junit.jupiter.api;

    opens client;
    opens server;
}