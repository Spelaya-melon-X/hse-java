package hse.java.commander.Examples;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.file.*;
import java.util.List;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FillListViewWithFiles extends Application {

    public List<String> listDir(Path dir) throws IOException {
        try (var stream = Files.list(dir)) {
            return stream
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    public void refreshList(ListView<String> listView, Path dir) throws IOException {
        listView.getItems().clear();
        listView.getItems().add("...");
        listView.getItems().addAll(listDir(dir));
    }

    @Override
    public void start(Stage primaryStage) {
        ListView<String> listView = new ListView<>();
        Path path = Paths.get("/Users/qwertz/Desktop/code/Java_codes/HSE/hse-java/commander/src/main/java/hse/java/commander/");

        try {
            refreshList(listView, path);
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибки
        }

        VBox vbox = new VBox(listView);
        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("File List Viewer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Запускаем JavaFX приложение
    }
}
