package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

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
        try {
            int season = this.season.getValue(), episode = this.episode.getValue();
            String[] tittleAndUrl = Main.getLink(season, episode);
            tittle.setText(tittleAndUrl[0]);
            result.setText(tittleAndUrl[1]);
            String s = season + " " + episode;
            FileWriter writer = new FileWriter(FILENAME);
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            result.setText("Error");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
