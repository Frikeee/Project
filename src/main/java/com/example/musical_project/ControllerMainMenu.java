package com.example.musical_project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerMainMenu extends View implements Initializable {

    @FXML
    private ImageView albumBtn, avatar, closeButton, horizontalLineButton, musicBtn, nextButton, personalBtn, playButton, previousButton, reloadButton, searchButton, volumeButton;

    @FXML
    private AnchorPane albumPane, musicPane, personalPane;

    @FXML
    private Button createAlbumButton, deleteMusic, deleteAlbum;

    @FXML
    private ListView<String> createAlbumList, albumList , musicAlbumList, musicList;;

    @FXML
    private Label endTimeText, beginTimeText, totalTime;

    @FXML
    private TextField searchField, nameAlbum;

    @FXML
    private Slider volumeSlider, progressBarMusic;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber = 0, numCreateSong = 0, positionMusic = 0;
    private double minutes = 0;
    private String stringLabel;
    private Boolean runningSong = false;
    private String music;
    private HashMap <String, File> fileList = new HashMap<>();
    private ArrayList<String> generalLibraryString = new ArrayList<>();
    private ArrayList<String> albumArray = new ArrayList<>();
    private ArrayList<String> listAlbumMusic = new ArrayList<>();
    DataBase dataBase = new DataBase();
    Duration allTimeMusic = new Duration(0.0);
    Media media;
    MediaPlayer mediaPlayer;

    @FXML
    public void clickLeftMenu() {
        personalBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changePane(1);
            }
        });
        musicBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changePane(2);
            }
        });
        albumBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changePane(3);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();
        directory = new File("music");
        files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                songs.add(file);
                stringLabel = songs.get(numCreateSong).toString();
                stringLabel = stringLabel.substring(6, stringLabel.length() - 4);
                fileList.put(stringLabel, file);
                generalLibraryString.add(stringLabel);
                numCreateSong++;
            }
        }
        musicList.getItems().addAll(generalLibraryString);
        media = new Media(fileList.get(generalLibraryString.get(0)).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.stop();
        clickLeftMenu();
        menuClicked();
        musicalBar();
        AlbumListAdd();
        musicList.setOnMouseClicked(mouseEvent ->  {
            musicalListView(musicList, createAlbumList, generalLibraryString, albumArray, true);
            reloadMusicList(musicList);
        });
        createAlbumList.setOnMouseClicked(mouseEvent -> {
            musicalListView(createAlbumList, musicList, albumArray, generalLibraryString, false);
            reloadMusicList(createAlbumList);
        });
        createAlbumButton.setOnAction(actionEvent -> {
            dataBase.createrAlbumFunction(albumArray, nameAlbum.getText());
            generalLibraryString.addAll(albumArray);
            allTimeMusic = new Duration(0);
            totalTime.setText("Общее время: 00:00");
            albumArray.clear();
            nameAlbum.clear();
            createAlbumList.getItems().clear();
            musicList.getItems().clear();
            musicList.getItems().setAll(generalLibraryString);
            AlbumListAdd();
        });
        albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                music = musicAlbumList.getSelectionModel().getSelectedItem();
                if(event.getClickCount() == 2){
                    musicAlbumList.getItems().clear();
                    musicAlbumList.getItems().addAll(dataBase.arrayMusicAlbum(albumList.getSelectionModel().getSelectedItem()));
                    System.out.println(albumList.getSelectionModel().getSelectedItem());
                }
            }
        });
        deleteAlbum.setOnAction(actionEvent -> {
            dataBase.deleteWhat(ConstRegistr.ALBUMNAME_TABLE, ConstRegistr.ALBUMNAME_NAME_PLAYLIST, albumList.getSelectionModel().getSelectedItem());
            AlbumListAdd();
            musicAlbumList.getItems().clear();
        });
        deleteMusic.setOnAction(actionEvent -> {
            dataBase.deleteWhat(ConstRegistr.SONGLIST_TABLE, ConstRegistr.SONGLIST_MUSICLIST, musicAlbumList.getSelectionModel().getSelectedItem());
            musicAlbumList.getItems().clear();
            musicAlbumList.getItems().addAll(dataBase.arrayMusicAlbum(albumList.getSelectionModel().getSelectedItem()));
        });
        musicAlbumList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Image image = new Image(String.valueOf(getClass().getResource("/image/play.png")));
                playButton.setImage(image);
                musicalListPlay(musicAlbumList);
            }
        });
    }

    private void AlbumListAdd() {
        albumList.getItems().clear();
        albumList.getItems().addAll(dataBase.arrayListAlbum());
        albumList.getItems().add(nameAlbum.getText());
    }

    public void musicalBar(){
        reloadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(0));
            }
        });
        volumeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(volumeSlider.isVisible()){
                    volumeSlider.setVisible(false);
                }
                else{
                    volumeSlider.setVisible(true);
                }
            }
        });
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (runningSong) {
                    Image image = new Image(String.valueOf(getClass().getResource("/image/play.png")));
                    playButton.setImage(image);
                    mediaPlayer.pause();
                    runningSong = false;
                } else {
                    Image image = new Image(String.valueOf(getClass().getResource("/image/pause.png")));
                    playButton.setImage(image);
                    mediaPlayer.play();
                    runningSong = true;
                }
            }
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(positionMusic < listAlbumMusic.size() - 1){
                    positionMusic++;
                    mediaPlayer.stop();
                    media = new Media(fileList.get(listAlbumMusic.get(positionMusic)).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
                    progressBar();
                    if(runningSong){
                        mediaPlayer.play();
                    }
                }
            }
        });
        previousButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(positionMusic > 0){
                    positionMusic--;
                    mediaPlayer.stop();
                    media = new Media(fileList.get(listAlbumMusic.get(positionMusic)).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
                    progressBar();
                    if(runningSong){
                        mediaPlayer.play();
                    }
                }
            }
        });
        volumeSlider.setOnMousePressed(event -> {
            mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
        });
        volumeSlider.setOnMouseDragged(event -> {
            mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
        });
    }

    public void menuClicked(){
        horizontalLineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = null;
                stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
                stage.setIconified(true);
            }
        });
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
    }

    public void musicalListView(ListView<String> clickedMenu, ListView <String> secondMenu, ArrayList <String> mainList, ArrayList <String> secondList, boolean isMainMenu){
        clickedMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                reloadMusicList(clickedMenu);
                music = clickedMenu.getSelectionModel().getSelectedItem();
                mediaPlayer.stop();
                Image image = new Image(String.valueOf(getClass().getResource("/image/play.png")));
                playButton.setImage(image);
                runningSong = false;
                media = new Media(fileList.get(music).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
                progressBar();
                if(event.getClickCount() == 2 && music != null){
                    mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                            if(isMainMenu) {
                                allTimeMusic = allTimeMusic.add(t1);
                            }
                            else {
                                allTimeMusic = allTimeMusic.subtract(t1);
                            }
                            totalTime.setText("Общее время: " + getTime(allTimeMusic));
                        }
                    });
                    secondList.add(music);
                    mainList.remove(music);
                    clickedMenu.getItems().remove(music);
                    secondMenu.getItems().addAll(music);
                }

            }
        });
    }

    public void reloadMusicList(ListView<String> clickedMenu){
        positionMusic = 0;
        listAlbumMusic.clear();
        listAlbumMusic.addAll(clickedMenu.getItems());
        positionMusic = clickedMenu.getSelectionModel().getSelectedIndex();
    }

    public void musicalListPlay(ListView<String> clickedMenu){
        clickedMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                reloadMusicList(clickedMenu);
                music = clickedMenu.getSelectionModel().getSelectedItem();
                mediaPlayer.stop();
                Image image = new Image(String.valueOf(getClass().getResource("/image/play.png")));
                playButton.setImage(image);
                runningSong = false;
                media = new Media(fileList.get(music).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                progressBar();
            }
        });
    }

    public void progressBar(){
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration currentValue) {
                progressBarMusic.setValue(currentValue.toSeconds());
                progressBarMusic.setMax(media.getDuration().toSeconds());
                bindCurrentTime();
            }
        });
        progressBarMusic.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBarMusic.getValue()));
            }
        });

        progressBarMusic.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBarMusic.getValue()));
            }
        });
    }

    private void bindCurrentTime() {
        beginTimeText.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(mediaPlayer.getCurrentTime());
            }
        }));
        endTimeText.textProperty().bind(Bindings.createObjectBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(mediaPlayer.getTotalDuration());
            }
        }));
    }

    private String getTime(Duration currentTime) {
        int minutes = (int) currentTime.toMinutes();
        int seconds = (int) currentTime.toSeconds();

        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

}
