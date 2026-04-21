import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyFirstrNotepadVersion2 extends Application {


    private TextArea textArea; //input

    // drawing canvas
    private Canvas canvas;
    private GraphicsContext gc;

    private StackPane centerPane;

    private boolean drawingMode = false;

    @Override
    public void start(Stage stage) {

        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPromptText("Type your notes here...");

        canvas = new Canvas(700, 450);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        // draw when mouse is pressed
        canvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.setOnMouseDragged(e -> { //
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        centerPane = new StackPane();
        centerPane.getChildren().add(textArea);

        Button openButton = new Button("Open");
        Button saveButton = new Button("Save HERE");
        Button wordCountButton = new Button("Word Count");
        Button switchButton = new Button("Drawing");
        Button saveDrawingButton = new Button("Save ");
        Button clearButton = new Button("Clear");


        openButton.setOnAction(e -> openFile(stage));

        saveButton.setOnAction(e -> saveFile(stage));

        wordCountButton.setOnAction(e -> showWordCount());

        switchButton.setOnAction(e -> {
            drawingMode = !drawingMode;
            centerPane.getChildren().clear();

            if (drawingMode) {
                centerPane.getChildren().add(canvas);
                switchButton.setText("Switch to Text");
            } else {
                centerPane.getChildren().add(textArea);
                switchButton.setText("Switch to Drawing");
            }
        });

        // save canvas as image
        saveDrawingButton.setOnAction(e -> saveDrawing(stage));

        // clear canvas
        clearButton.setOnAction(e -> clearCanvas());

        Label titleLabel = new Label("JW notepad!!!");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.getChildren().addAll(
                openButton, saveButton, wordCountButton,
                switchButton, saveDrawingButton, clearButton
        );

        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(titleLabel, buttonBar);

        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(centerPane);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: Red;");


        Scene scene = new Scene(root, 850, 600);

        stage.setTitle("MyFirstNotepadVersion2");
        stage.setScene(scene);
        stage.show();
    }

    private void openFile(Stage stage) { // main opener
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.clear();

                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }

                showAlert("Success", "File opened successfully.");
            } catch (IOException ex) {
                showAlert("Error", "Could not open file.");
            }
        }
    }

    private void saveFile(Stage stage) { // save buyt file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
                showAlert("Success", "File saved successfully.");
            } catch (IOException ex) {
                showAlert("Error", "Could not save file.");
            }
        }
    }

    private void showWordCount() { // num counts
        String text = textArea.getText().trim();

        int count = 0;
        if (!text.isEmpty()) {
            String[] words = text.split("\\s+");
            count = words.length;
        }

        showAlert("Word Count", "Total words: " + count);
    }

    private void saveDrawing(Stage stage) { // save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                WritableImage image = new WritableImage(
                        (int) canvas.getWidth(),
                        (int) canvas.getHeight()
                );

                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.WHITE);

                canvas.snapshot(params, image);

                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

                showAlert("Success", "Drawing saved successfully.");
            } catch (IOException ex) {
                showAlert("Error", "Could not save drawing.");
            }
        }
    }

    // del stuff fpr fix later
    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
    }

    private void showAlert(String title, String message) { // error make
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}