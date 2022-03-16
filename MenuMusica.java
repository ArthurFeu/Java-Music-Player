package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MenuMusica extends Application {
    //cena
    private BorderPane root = new BorderPane(); //estrutura que proporciona a aparencia do programa
    private Scene scene = new Scene(root, 500, 300); //cena principal, que da a vida ao programa
    private HBox root2 = new HBox();
    private final Pane space = new Pane();
    private final Pane space2 = new Pane();

    //arquivos
    private final FileChooser filechooser = new FileChooser(); //meio para acessar os arquivos do computador
    private File currentfile; //arquivo
    private Media media; //arquivo de audio
    private MediaPlayer mediaplayer; //player do arquivo de audio (detalhe = ambos sao instanciados dentro da area de atuaçao, ja que eles nao podem apresentar um valor fixo

    //visual
    private final Button playbutton = new Button("▷"); //botao play
    private final Button skipbutton = new Button("▷▷"); //botao skip
    private final Button pausebutton = new Button("⏸"); //botao , pause
    private MenuBar menubar = new MenuBar(); //barra do menu localizada no topo
    private Menu filemenu = new Menu("Files"); //seção dentro da menubar
    private MenuItem selectmusic = new MenuItem("Select a music"); //seção dentro da filemenu
    private ListView<String> playlist = new ListView<>(); //musicas na direita
    private ArrayList<File> songsfiles = new ArrayList<>(); //lista com todos os arquivos que o usuario selecionou no filechooser
    private Text actionstatus = new Text(); //mensagem de erro ou de sucesso, em relaçao ao arquivo

    //variaveis de auxiliov temporario
    private final String[] namefile = new String[1]; //variavel que pega o path da musica
    private static int index; //index que auxilia na construçao do songsfiles

    public void start(final Stage playermusica) {
        index = 0;
        root2.setPadding(new Insets(100, 50, 50, 50));

        //configurando o handler do selectmusic, que seleciona os arquivos .mp3 ou .mp4, de acordo com os extfilters do filechooser
        EventHandler<ActionEvent> musicevent = event -> {
            int songvalidation = 0;
            File selectedfile = filechooser.showOpenDialog(playermusica);
            if (songsfiles.size() == 0){
                songsfiles.add(index, selectedfile);
                playlist.getItems().add(selectedfile.getName());
                index++;
            }else {
                for (File songsfile : songsfiles) {
                    if (songsfiles.contains(selectedfile))
                        songvalidation++;
                }
                if (songvalidation == 0) {
                    songsfiles.add(index, selectedfile);
                    index++;
                    playlist.getItems().add(selectedfile.getName());
                }
            }
        };
        selectmusic.setOnAction(musicevent);

        //configurando o handler da playlist, que pega o path da musica que o mouse clicou, para que ela seja a proxima musica tocada
        playlist.setOnMouseClicked(mouseEvent -> {
            String filename = playlist.getSelectionModel().getSelectedItem();
            for (int pos = 0; pos < index; pos++) {
                File file = songsfiles.get(pos);
                if (stringCompare(file.getName(), filename) == 0){
                    namefile[0] = file.getAbsolutePath();
                }
            }
        });

        //configurando o handler do playmusic, que pega o path da musica selecionada no handler anterior e toca ela
        playbutton.setOnAction(
                actionEvent -> {
                    String path = namefile[0];
                    if (path == null) {
                        actionstatus.setText("You didn't select a file!");
                        actionstatus.setFill(Color.RED);
                    } else {
                        File file = new File(path);
                        currentfile = file;
                        actionstatus.setText("Current song: " + file.getName());
                        actionstatus.setFill(Color.GREEN);
                        media = new Media(file.toURI().toString());
                        mediaplayer = new MediaPlayer(media);
                        mediaplayer.stop();
                        mediaplayer.play();
                    }
                }
        );

        pausebutton.setOnAction(
                actionEvent -> {
                    String path = namefile[0];
                    if (path == null) {
                        actionstatus.setText("You didn't select a file!");
                        actionstatus.setFill(Color.RED);
                    } else {
                        mediaplayer.pause();
                    }
                }
        );

        //configurando o handler do skipmusic, que pega o index da musica atual e toca a musica com o index + 1
        skipbutton.setOnAction(
                actionEvent -> {
                    mediaplayer.stop();
                    int newindex = (songsfiles.indexOf(currentfile)) + 1;
                    System.out.println(newindex);
                    File file = songsfiles.get(newindex);
                    media = new Media(file.toURI().toString());
                    mediaplayer = new MediaPlayer(media);
                    mediaplayer.play();
                }
        );

        //configurando o filechooser
        configuringFileChooser(filechooser);

        //botoes
        space.setMinSize(10, 1);
        space2.setMinSize(10, 1);
        playbutton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        playbutton.setStyle("-fx-background-color: #ffffff; -fx-border-color: #5e83e0; -fx-border-width: 3px;");
        skipbutton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        skipbutton.setStyle("-fx-background-color: #ffffff; -fx-border-color: #5e83e0; -fx-border-width: 3px;");
        pausebutton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        pausebutton.setStyle("-fx-background-color: #ffffff; -fx-border-color: #5e83e0; -fx-border-width: 3px;");

        //setando a cena
        HBox.setHgrow(space, Priority.ALWAYS);
        HBox.setHgrow(space2, Priority.ALWAYS);
        root2.getChildren().addAll(playbutton, space2);
        root2.getChildren().addAll(pausebutton, space, skipbutton);
        root.setTop(menubar);
        root.setCenter(root2);
        root.setBottom(actionstatus);
        root.setRight(playlist);
        filemenu.getItems().addAll(selectmusic);
        menubar.getMenus().addAll(filemenu);

        //rodando o codigo
        playermusica.setScene(scene);
        playermusica.show();
    }
    private void configuringFileChooser(FileChooser filechooser) {
        //dinamica do filechooser
        filechooser.setTitle("Select a song to be played");
        filechooser.setInitialDirectory(new File("C:\\Users\\joaot\\Desktop\\musicas"));
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4")
        );
    }
    private static int stringCompare(String current, String other){
        //comparaçao basica de strings, que auxilia no handler da listview
        int currentlength = current.length();
        int otherlength = other.length();
        int minimunlength = Math.min(currentlength, otherlength);

        for (int i = 0; i < minimunlength; i++) {
            int currentch = current.charAt(i);
            int otherch = other.charAt(i);

            if (currentch != otherch) {
                return currentch - otherch;
            }
        }
        if (currentlength != otherlength) {
            return currentlength - otherlength;
        }
        else {
            return 0;
        }
    }
    //rodando o codigo
    public static void main(String[] args){ launch(args);  }
}