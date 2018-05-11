/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import sportbook.domain.Activity;
import sportbook.domain.Sportbook;
import sportbook.domain.Sportbook.StatisticsNode;

/**
 * Class creates the statistics view where users can see their monthly statistics.
 * 
 * @author mshroom
 */
public class StatisticsView {

    private Sportbook sportbook;
    private Calendar calendar;
    private SimpleDateFormat simpleDate;
    private VBox activityBox;
    private VBox workoutBox;
    private VBox completedBox;
    private VBox uncompletedBox;
    private Label error;

    public StatisticsView(Sportbook sportbook, Calendar calendar) {
        this.sportbook = sportbook;
        this.calendar = calendar;
        this.simpleDate = new SimpleDateFormat("MM/yyyy");
    }

    /**
     * Method creates the statistics view.
     * 
     * @return the view as a Parent object
     */
    public Parent getView() {
        Button previousButton = new Button("Previous month");
        Button nextButton = new Button("Next month");
        Label dateLabel = new Label(simpleDate.format(calendar.getTime()));
        error = new Label("");
        GridPane selectionGridPane = new GridPane();

        selectionGridPane.add(previousButton, 0, 0);
        selectionGridPane.add(dateLabel, 1, 0);
        selectionGridPane.add(nextButton, 2, 0);
        selectionGridPane.add(error, 0, 1);

        selectionGridPane.setAlignment(Pos.TOP_CENTER);
        selectionGridPane.setVgap(10);
        selectionGridPane.setHgap(10);
        selectionGridPane.setPadding(new Insets(20, 20, 20, 20));

        previousButton.setOnAction((event) -> {
            calendar.add(Calendar.MONTH, -1);
            dateLabel.setText(simpleDate.format(calendar.getTime()));
            drawStatistics();
        });

        nextButton.setOnAction((event) -> {
            calendar.add(Calendar.MONTH, 1);
            dateLabel.setText(simpleDate.format(calendar.getTime()));
            drawStatistics();
        });

        Label activityLabel = new Label("Activity");
        Label workoutsLabel = new Label("All workouts");
        Label completedLabel = new Label("Completed goals");
        Label uncompletedLabel = new Label("Uncompleted goals");
        GridPane statisticsGridPane = new GridPane();

        statisticsGridPane.add(activityLabel, 0, 0);
        statisticsGridPane.add(workoutsLabel, 1, 0);
        statisticsGridPane.add(completedLabel, 2, 0);
        statisticsGridPane.add(uncompletedLabel, 3, 0);

        statisticsGridPane.setAlignment(Pos.TOP_CENTER);
        statisticsGridPane.setVgap(10);
        statisticsGridPane.setHgap(10);
        statisticsGridPane.setPadding(new Insets(20, 20, 20, 20));

        ScrollPane activityScrollBar = new ScrollPane();
        ScrollPane workoutScrollBar = new ScrollPane();
        ScrollPane completedScrollBar = new ScrollPane();

        ScrollPane uncompletedScrollBar = new ScrollPane();

        activityBox = new VBox();
        workoutBox = new VBox();
        completedBox = new VBox();
        uncompletedBox = new VBox();
        drawStatistics();

        activityScrollBar.setContent(activityBox);
        workoutScrollBar.setContent(workoutBox);
        completedScrollBar.setContent(completedBox);
        uncompletedScrollBar.setContent(uncompletedBox);

        statisticsGridPane.add(activityScrollBar, 0, 1);
        statisticsGridPane.add(workoutScrollBar, 1, 1);
        statisticsGridPane.add(completedScrollBar, 2, 1);
        statisticsGridPane.add(uncompletedScrollBar, 3, 1);

        ColumnConstraints column1 = new ColumnConstraints(230);
        statisticsGridPane.getColumnConstraints().add(column1);

        for (int i = 1; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(140);
            statisticsGridPane.getColumnConstraints().add(column);
        }

        GridPane viewGridPane = new GridPane();
        viewGridPane.add(selectionGridPane, 0, 0);
        viewGridPane.add(statisticsGridPane, 0, 1);
        return viewGridPane;
    }

    /**
     * Method refreshes the statistics table.
     */
    private void drawStatistics() {
        activityBox.getChildren().clear();
        workoutBox.getChildren().clear();
        completedBox.getChildren().clear();
        uncompletedBox.getChildren().clear();
        List<StatisticsNode> statistics = sportbook.createStatistics(calendar.getTime());
        if (statistics != null) {
            statistics.forEach(node -> {
                Label activity = new Label(node.getActivity());
                Label workouts = new Label(node.getWorkouts());
                Label completed = new Label(node.getCompleted());
                Label uncompleted = new Label(node.getUncompleted());

                activityBox.getChildren().add(activity);
                workoutBox.getChildren().add(workouts);
                completedBox.getChildren().add(completed);
                uncompletedBox.getChildren().add(uncompleted);
            });
        } else {
            error.setText("A problem occurred while accessing the database)");
        }
    }
}
