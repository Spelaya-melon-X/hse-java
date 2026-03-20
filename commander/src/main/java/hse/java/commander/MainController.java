package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    private ListView<String> activeList;
    private Path activeDir;

    @FXML public ListView<String> left;
    @FXML public ListView<String> right;
    @FXML public Button move;
    @FXML public Button copy;
    @FXML public Button delete;

    private Path leftDir;
    private Path rightDir;


    @FXML public Label leftPathBar;
    @FXML public Label rightPathBar;
    @FXML public VBox leftPanel;
    @FXML public VBox rightPanel;


    public void setInitialDirs(Path leftStart, Path rightStart) {
        this.leftDir = leftStart;
        this.rightDir = rightStart;
        refreshList(left, leftDir);
        refreshList(right, rightDir);
    }

    public void initialize() {
        left.setOnMouseClicked(event -> {
            activeList = left;
            activeDir = leftDir;
            leftPanel.getStyleClass().remove("panel-active");
            leftPanel.getStyleClass().add("panel-active");
            if (event.getClickCount() == 2) {
                handleDoubleClick(left, leftDir, true);
            }
        });

        right.setOnMouseClicked(event -> {
            activeList = right;
            activeDir = rightDir;
            rightPanel.getStyleClass().add("panel-active");
            rightPanel.getStyleClass().remove("panel-active");
            if (event.getClickCount() == 2) {
                handleDoubleClick(right, rightDir, false);
            }
        });

        copy.setOnMouseClicked(event -> handleCopy());
        move.setOnMouseClicked(event -> handleMove());
        delete.setOnMouseClicked(event -> handleDelete());
    }

    private void handleDoubleClick(ListView<String> listView, Path currentDir, boolean isLeft) {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (selected.equals("...")) {
            Path parent = currentDir.getParent();
            if (parent != null) {
                if (isLeft) {
                    leftDir = parent;
                    activeDir = leftDir;
                    refreshList(left, leftDir);
                } else {
                    rightDir = parent;
                    activeDir = rightDir;
                    refreshList(right, rightDir);
                }
            }
        } else {
            Path target = currentDir.resolve(selected);
            if (Files.isDirectory(target)) {
                if (isLeft) {
                    leftDir = target;
                    activeDir = leftDir;
                    refreshList(left, leftDir);
                } else {
                    rightDir = target;
                    activeDir = rightDir;
                    refreshList(right, rightDir);
                }
            }
        }
    }

    private void handleCopy() {
        if (activeList == null || activeDir == null) return;
        String selected = activeList.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("...")) return;

        Path source = activeDir.resolve(selected);
        Path targetDir = (activeList == left) ? rightDir : leftDir;
        Path target = targetDir.resolve(selected);

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            refreshList(left, leftDir);
            refreshList(right, rightDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMove() {
        if (activeList == null || activeDir == null) return;
        String selected = activeList.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("...")) return;

        Path source = activeDir.resolve(selected);
        Path targetDir = (activeList == left) ? rightDir : leftDir;
        Path target = targetDir.resolve(selected);

        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            refreshList(left, leftDir);
            refreshList(right, rightDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete() {
        if (activeList == null || activeDir == null) return;
        String selected = activeList.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("...")) return;

        Path toDelete = activeDir.resolve(selected);
        try {
            Files.deleteIfExists(toDelete);
            refreshList(left, leftDir);
            refreshList(right, rightDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshList(ListView<String> listView, Path dir) {
        listView.getItems().clear();
        listView.getItems().add("...");
        try (var stream = Files.list(dir)) {
            List<String> names = stream
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());
            listView.getItems().addAll(names);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (listView == left && leftPathBar != null)  leftPathBar.setText(dir.toString());
        if (listView == right && rightPathBar != null) rightPathBar.setText(dir.toString());
    }
}