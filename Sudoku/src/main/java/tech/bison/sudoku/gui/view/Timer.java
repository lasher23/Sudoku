package tech.bison.sudoku.gui.view;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Timer extends VBox {
	private long time = 0;
	private Label label = new Label("00:00:00");
	private ScheduledService<Void> service;

	public Timer() {
		label.setFont(new Font(30));
		getChildren().add(label);
		setAlignment(Pos.CENTER);
	}

	public void startTimer() {
		service = new ScheduledService<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						time += 1000;
						Timestamp timestamp = new Timestamp(time);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
						Platform.runLater(() -> label.setText(timestamp.toLocalDateTime().format(formatter)));
						return null;
					}
				};
			}
		};
		service.setPeriod(Duration.seconds(1));
		service.start();
	}

	public void stopTimer() {
		if (service != null) {
			service.cancel();
		}
	}
}
