package hse.java.commander.Examples;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Import Button
import javafx.scene.layout.VBox; // Import VBox
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    Integer countTap = 0 ;

    public static void main(String[] args) {
        System.out.println("Launching Application");
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        System.out.println("Application inits");
        super.init();
    }

    @Override
    public void start(Stage stage) {
        System.out.println("Application starts");

        // Create button and set action
        Button btn = new Button("Hello");

        btn.setOnAction(event -> System.out.println("Button clicked!" + countTap++) );

        // Use VBox for layout
        VBox vbox = new VBox(10); // Vertical spacing of 10
        vbox.getChildren().add(btn); // Add button to VBox

        List<String> unnamedParams = getParameters().getUnnamed();
        for (String param : unnamedParams) {
            Text text = new Text(param);
            vbox.getChildren().add(text); // Add text parameters to VBox
        }

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.setTitle("JavaFX Application");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Application stops");
        super.stop();
    }
}
