package Routing;

import Routing.Controller.MainController;
import Routing.Model.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application{
    private Stage primaryStage;
    private MainController mainController;
    private MainModel mainModel;
    private boolean simulazioneAttiva;

    public boolean isSimulazioneAttiva() {
        return simulazioneAttiva;
    }

    public void setSimulazioneAttiva(boolean simulazioneAttiva) {
        this.simulazioneAttiva = simulazioneAttiva;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FRANCISCO PackeTracker™");

        inizializza();
        
    }

    public static void main(String[] args)  {
        launch(args);
    }

    private void inizializza() {
        try {

            mainModel = new MainModel();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/Routing/View/Main.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);

            mainController = loader.getController(); // loader ha già istanziato il controller indicato in mainGui
            mainController.setMainApp(this);

            //test();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainModel getMainModel() {
        return mainModel;
    }

}
