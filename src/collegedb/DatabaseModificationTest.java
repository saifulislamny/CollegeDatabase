package collegedb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DatabaseModificationTest extends Application {
    @Override
    public void start(Stage ps) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        final Canvas canvas = new Canvas(1366, 768);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        PieChart pc = new PieChart();
        pc.draw(graphicsContext);

        root.getChildren().add(canvas);
        ps.setScene(scene);
        ps.show();
    }
    public static void main(String[] args) {
        DatabaseModification.connectToDatabase();
        DatabaseModification.deletePreviousTables(); // if previous tables exist, delete them
        DatabaseModification.createTables();

        DatabaseModification.insertIntoStudents(23456789, "Saiful", "Islam",
                "sislam008@citymail.cuny.edu", 'M');
        DatabaseModification.insertIntoCourses("CSC 22100", "Software Design Laboratory (Lecture)",
                "CSC");
//        DatabaseModification.insertIntoClasses("CSC 22100", 23456789, "P", 2020,
//                "Spring", 'A');

        

        DatabaseModification.showTables();
        DatabaseModification.tableEnrolledInCSc22100Spr2020();
        launch(args);
        DatabaseModification.closeDatabase();
    }
}
