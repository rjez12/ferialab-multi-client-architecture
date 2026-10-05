package org.ferialab.evaluator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class FeriaLabEvaluator extends Application {
    private static final String DEFAULT_API = "http://localhost:8080/ferialab/api/v1";
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final ObjectMapper json = new ObjectMapper();
    private final ListView<Project> list = new ListView<>();
    private final Label status = new Label("Conectando con FeriaLab…");

    @Override
    public void start(Stage stage) {
        TextField search = new TextField();
        search.setPromptText("Buscar por proyecto, categoría o equipo");
        search.textProperty().addListener((observable, oldValue, newValue) -> filter(newValue));
        list.setCellFactory(view -> new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (empty || project == null) setText(null);
                else setText(project.title() + "\n" + project.category() + " · " + project.team() + "\n" + project.summary());
            }
        });
        VBox header = new VBox(8, new Label("FERIALAB / EVALUADOR"), search, status);
        header.setPadding(new Insets(20));
        BorderPane root = new BorderPane(list, header, null, null, null);
        root.setPadding(new Insets(12));
        stage.setTitle("FeriaLab · Catálogo de proyectos");
        stage.setScene(new Scene(root, 760, 560));
        stage.show();
        loadProjects();
    }

    private List<Project> projects = List.of();

    private void loadProjects() {
        String base = System.getenv().getOrDefault("FERIALAB_API", DEFAULT_API);
        HttpRequest request = HttpRequest.newBuilder(URI.create(base + "/proyectos?publicados=true"))
                .timeout(Duration.ofSeconds(10)).header("Accept", "application/json").GET().build();
        http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() / 100 != 2) throw new IllegalStateException("La API respondió " + response.statusCode());
                    try { return json.readValue(response.body(), new TypeReference<List<Project>>() {}); }
                    catch (Exception exception) { throw new IllegalStateException("No se pudo leer el catálogo", exception); }
                })
                .whenComplete((records, failure) -> Platform.runLater(() -> {
                    if (failure != null) status.setText("No se pudo conectar. Revisa que la API esté activa.");
                    else { projects = records; status.setText(records.size() + " proyectos publicados"); filter(""); }
                }));
    }

    private void filter(String text) {
        String query = text == null ? "" : text.strip().toLowerCase();
        list.setItems(FXCollections.observableArrayList(projects.stream()
                .filter(project -> (project.title() + " " + project.category() + " " + project.team() + " " + project.summary())
                        .toLowerCase().contains(query))
                .toList()));
    }

    public record Project(String id, String title, String category, String summary, String team, String stage,
                          String urlRepositorio) {}

    public static void main(String[] args) { launch(args); }
}
