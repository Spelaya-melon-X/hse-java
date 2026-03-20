module hse.java.commander {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;

    opens hse.java.commander to javafx.fxml;
    exports hse.java.commander;
    exports hse.java.commander.Examples;
    opens hse.java.commander.Examples to javafx.fxml;
}