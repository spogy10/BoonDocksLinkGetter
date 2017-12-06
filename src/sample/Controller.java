package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable{
    final String FILENAME = "BoonDocks";
    @FXML private ChoiceBox<Integer> episode, season;
    @FXML private TextField result, tittle;
    private int seasonNum = 2, episodeNum = 1;

    @FXML
    private void Go(){
        result.setText("");
        tittle.setText("");
        int season = this.season.getValue(), episode = this.episode.getValue();
        Service<String[]> service = new Service<>() {
            @Override
            protected Task<String[]> createTask() {
                return new Main.GetWebData(season, episode);
            }
        };
        service.restart();
        service.setOnSucceeded(e -> {
            String[] tittleAndUrl = service.getValue();
            tittle.setText(tittleAndUrl[0]);
            result.setText(tittleAndUrl[1]);
            StringSelection selection = new StringSelection(tittleAndUrl[1]);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            String s = season + " " + episode;
            FileWriter writer = null;
            try {
                writer = new FileWriter(FILENAME);
                writer.write(s);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                result.setText("Error");
            }
        });
        service.setOnFailed(e -> {
            result.setText("Error");
        });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tittle.setEditable(false);
        result.setEditable(false);
        File file = new File(FILENAME);
        if(file.exists() && file.isFile()){
            try {
                Scanner in = new Scanner(file);
                in.useDelimiter(" ");
                seasonNum = in.nextInt();
                episodeNum = in.nextInt();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        setSeasonChoiceBox();
        season.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                setEpisodeChoiceBox();
            }
        });
        setEpisodeChoiceBox();
    }

    private void setSeasonChoiceBox() {
        season.getItems().addAll(1, 2, 3, 4);
        season.setValue(seasonNum);
    }

    private void setEpisodeChoiceBox() {
        episode.getItems().clear();
        if(season.getValue() == 4)
            episode.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        else
            episode.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        episode.setValue(episodeNum);
    }
}
