import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    private TableView<Process> table = new TableView<>();
    private final List<Process> processList = new ArrayList<>();
    private Pane ganttPane = new Pane();
    private Label lblAWT = new Label("AWT: -");
    private Label lblATAT = new Label("ATAT: -");
    private Label lblCPU = new Label("CPU Util: -");
    private Label lblThroughput = new Label("Throughput: -");
    private ChoiceBox<String> algoChoice;
    private TextField quantumField;
    private Slider speedSlider;
    private ToggleButton themeToggle;
    private Timeline timeline;
    private List<Scheduler.Slice> slices = new ArrayList<>();
    private Map<Integer, Color> colorMap = new HashMap<>();
    private boolean dark = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OS Scheduling Visualizer (JavaFX)");

        HBox topControls = buildTopControls();

        buildTable();
        VBox left = new VBox(8, new Label("Processes"), table, buildTableButtons());
        left.setPadding(new Insets(8));
        left.setPrefWidth(360);

        VBox center = new VBox(6);
        ganttPane.setPrefSize(800, 320);
        ganttPane.setStyle("-fx-border-color: #cccccc; -fx-background-color: white;");
        center.getChildren().addAll(new Label("Gantt Chart"), ganttPane);
        center.setPadding(new Insets(8));

        VBox right = new VBox(8, new Label("Metrics"), lblAWT, lblATAT, lblCPU, lblThroughput);
        right.setPadding(new Insets(8));
        right.setPrefWidth(240);

        BorderPane root = new BorderPane();
        root.setTop(topControls);
        root.setLeft(left);
        root.setCenter(center);
        root.setRight(right);

        Scene scene = new Scene(root, 1320, 620);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        applyTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox buildTopControls() {
        HBox box = new HBox(8);
        box.setPadding(new Insets(8));

        TextField idF = new TextField(); idF.setPrefWidth(60); idF.setPromptText("ID");
        TextField atF = new TextField(); atF.setPrefWidth(80); atF.setPromptText("AT");
        TextField btF = new TextField(); btF.setPrefWidth(80); btF.setPromptText("BT");
        TextField prF = new TextField(); prF.setPrefWidth(80); prF.setPromptText("PR");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idF.getText());
                int at = Integer.parseInt(atF.getText());
                int bt = Integer.parseInt(btF.getText());
                int pr = prF.getText().isEmpty()?0:Integer.parseInt(prF.getText());
                Process p = new Process(id, at, bt, pr);
                processList.add(p);
                table.getItems().add(p);
                idF.clear(); atF.clear(); btF.clear(); prF.clear();
            } catch (Exception ex) { alert("Invalid input"); }
        });

        Button random = new Button("Random 5");
        random.setOnAction(e -> {
            Random r = new Random();
            int base = processList.size()*10 + 1;
            for (int i=0;i<5;i++){
                Process p = new Process(base+i, r.nextInt(5), 1 + r.nextInt(8), 1 + r.nextInt(5));
                processList.add(p); table.getItems().add(p);
            }
        });

        algoChoice = new ChoiceBox<>();
        algoChoice.getItems().addAll("FCFS", "SJF (Non-preemptive)","SJF (Preemptive)",
                "Priority (Non-preemptive)","Priority (Preemptive)","Round Robin");
        algoChoice.setValue("FCFS");

        quantumField = new TextField(); quantumField.setPromptText("Quantum"); quantumField.setPrefWidth(80);

        Button run = new Button("Run");
        run.setOnAction(e -> runSimulation());

        Button play = new Button("Play"), pause = new Button("Pause"), step = new Button("Step");
        play.setOnAction(e -> resume());
        pause.setOnAction(e -> pause());
        step.setOnAction(e -> stepOnce());

        speedSlider = new Slider(0.25, 3.0, 1.0); speedSlider.setPrefWidth(160);
        Label speedLbl = new Label("Speed");

        Button save = new Button("Save CSV"); save.setOnAction(e -> saveCSV());
        Button load = new Button("Load CSV"); load.setOnAction(e -> loadCSV());
        Button export = new Button("Export PNG"); export.setOnAction(e -> exportPNG());

        themeToggle = new ToggleButton("Dark");
        themeToggle.setOnAction(e -> {
            dark = themeToggle.isSelected();
            applyTheme(themeToggle.getScene());
        });

        box.getChildren().addAll(
                new Label("ID"), idF,
                new Label("AT"), atF,
                new Label("BT"), btF,
                new Label("PR"), prF,
                addBtn, random,
                new Label("Algo"), algoChoice, quantumField,
                run, play, pause, step,
                speedLbl, speedSlider,
                save, load, export, themeToggle
        );
        return box;
    }

    private void buildTable() {
        TableColumn<Process, Number> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().id));
        TableColumn<Process, Number> c2 = new TableColumn<>("Arrival");
        c2.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().arrival));
        TableColumn<Process, Number> c3 = new TableColumn<>("Burst");
        c3.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().burst));
        TableColumn<Process, Number> c4 = new TableColumn<>("Priority");
        c4.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().priority));
        table.getColumns().addAll(c1,c2,c3,c4);
        table.setPrefHeight(360);
    }

    private HBox buildTableButtons() {
        HBox hb = new HBox(8);
        Button del = new Button("Delete");
        del.setOnAction(e -> {
            Process s = table.getSelectionModel().getSelectedItem();
            if (s!=null) { processList.remove(s); table.getItems().remove(s); }
        });
        Button edit = new Button("Edit Burst");
        edit.setOnAction(e -> {
            Process s = table.getSelectionModel().getSelectedItem();
            if (s!=null) {
                TextInputDialog dlg = new TextInputDialog(String.valueOf(s.burst));
                dlg.setTitle("Edit Burst"); dlg.setHeaderText("Edit burst for P"+s.id);
                dlg.setContentText("Burst:");
                dlg.showAndWait().ifPresent(ans -> {
                    try { s.burst = Integer.parseInt(ans); s.remaining = s.burst; table.refresh(); } catch (Exception ex) { alert("Invalid"); }
                });
            }
        });
        hb.getChildren().addAll(edit, del);
        return hb;
    }

    private void runSimulation() {
        stopTimeline();
        ganttPane.getChildren().clear();
        colorMap.clear();
        if (processList.isEmpty()) { alert("No processes"); return; }

        // copy & assign colors
        List<Process> copy = processList.stream().map(Process::copy).collect(Collectors.toList());
        for (Process p : copy) {
            if (p.colorHex == null) p.colorHex = randomHex();
            colorMap.put(p.id, Color.web(p.colorHex));
        }

        // pick algo
        Scheduler.Algo algo = Scheduler.Algo.FCFS;
        String sel = algoChoice.getValue();
        int quantum = 2;
        try { quantum = Integer.parseInt(quantumField.getText()); } catch (Exception ignored) { }
        switch (sel) {
            case "FCFS": algo = Scheduler.Algo.FCFS; break;
            case "SJF (Non-preemptive)": algo = Scheduler.Algo.SJF_NONPREEMPTIVE; break;
            case "SJF (Preemptive)": algo = Scheduler.Algo.SJF_PREEMPTIVE; break;
            case "Priority (Non-preemptive)": algo = Scheduler.Algo.PRIORITY_NONPREEMPTIVE; break;
            case "Priority (Preemptive)": algo = Scheduler.Algo.PRIORITY_PREEMPTIVE; break;
            case "Round Robin": algo = Scheduler.Algo.ROUND_ROBIN; break;
        }

        slices = Scheduler.simulate(copy, algo, quantum);

        // compute metrics summary
        int simulationEnd = slices.stream().mapToInt(s -> s.start + s.duration).max().orElse(0);
        double totalWT = 0, totalTAT = 0; int cpuBusy = 0;
        for (Process p : copy) { totalWT += p.waiting; totalTAT += p.turnaround; cpuBusy += p.burst; }
        lblAWT.setText(String.format("AWT: %.2f", totalWT / copy.size()));
        lblATAT.setText(String.format("ATAT: %.2f", totalTAT / copy.size()));
        lblCPU.setText(String.format("CPU Util: %.2f%%", simulationEnd>0?100.0*cpuBusy/simulationEnd:0));
        lblThroughput.setText(String.format("Throughput: %.2f proc/unit", copy.size()/(double)Math.max(1,simulationEnd)));

        drawGantt(0);
    }

    private void drawGantt(int upto) {
        ganttPane.getChildren().clear();
        int y = 10;
        int height = 40;
        for (int i=0;i<upto && i<slices.size();i++) {
            Scheduler.Slice s = slices.get(i);
            Rectangle r = new Rectangle(s.start*30, y, s.duration*30, height);
            r.setFill(colorMap.get(s.pid));
            r.setStroke(Color.BLACK);
            ganttPane.getChildren().add(r);
            Label l = new Label("P"+s.pid);
            l.setLayoutX(s.start*30 + 5);
            l.setLayoutY(y + 10);
            ganttPane.getChildren().add(l);
        }
    }

    private void stepOnce() {
        if (timeline==null) timeline = new Timeline();
        timeline.stop();
        int current = ganttPane.getChildren().size()/2; // rough estimate
        if (current<slices.size()) drawGantt(current+1);
    }

    private void resume() {
        if (slices.isEmpty()) return;
        stopTimeline();
        timeline = new Timeline();
        timeline.setCycleCount(slices.size());
        for (int i=0;i<slices.size();i++){
            int idx = i;
            KeyFrame kf = new KeyFrame(Duration.seconds(i/speedSlider.getValue()), e -> drawGantt(idx+1));
            timeline.getKeyFrames().add(kf);
        }
        timeline.play();
    }

    private void pause() { if (timeline!=null) timeline.pause(); }
    private void stopTimeline() { if (timeline!=null) timeline.stop(); }

    private void saveCSV() {
        FileChooser fc = new FileChooser(); fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File f = fc.showSaveDialog(null); if (f!=null) {
            try { CSVUtil.saveProcessesCSV(f, processList); } catch (Exception e){ alert(e.getMessage()); }
        }
    }

    private void loadCSV() {
        FileChooser fc = new FileChooser(); fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File f = fc.showOpenDialog(null); if (f!=null) {
            try { List<Process> loaded = CSVUtil.loadProcessesCSV(f); processList.clear(); table.getItems().clear(); processList.addAll(loaded); table.getItems().addAll(loaded); }
            catch (Exception e){ alert(e.getMessage()); }
        }
    }

    private void exportPNG() {
        WritableImage img = ganttPane.snapshot(new SnapshotParameters(), null);
        FileChooser fc = new FileChooser(); fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files","*.png"));
        File f = fc.showSaveDialog(null); if (f!=null) {
            try { ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(img, null),"png",f); }
            catch (Exception e){ alert(e.getMessage()); }
        }
    }

    private void applyTheme(Scene scene) {
        if (scene==null) return;
        if (dark) scene.getRoot().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
        else scene.getRoot().setStyle("");
    }

    private String randomHex() {
        Random r = new Random();
        return String.format("#%02x%02x%02x", r.nextInt(200), r.nextInt(200), r.nextInt(200));
    }

    private void alert(String msg) { Platform.runLater(()->{
        Alert a = new Alert(Alert.AlertType.WARNING,msg); a.showAndWait();
    }); }

    public static void main(String[] args) {
        launch(args);
    }
}
