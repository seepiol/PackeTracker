package Routing;

import Routing.Controller.MainController;
import Routing.Model.Collegamento;
import Routing.Model.Interfaccia;
import Routing.Model.MainModel;
import Routing.Model.Router;
import Routing.View.DragResizeMod;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application{
    private Stage primaryStage;
    private MainController mainController;
    private MainModel mainModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("NetSim (PreAlpha)");

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

            test();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void test(){
        // creazione router
        Router r1 = new Router("r1", mainController);
        Router r2 = new Router("r2", mainController);
        Router r3 = new Router("r3", mainController);
        Router r4 = new Router("r4", mainController);

        // creazione interfacce
        Interfaccia eth1r1 = new Interfaccia("eth1", "1");
        Interfaccia eth2r1 = new Interfaccia("eth2", "6");
        Interfaccia eth3r1 = new Interfaccia("eth3", "7");
        Interfaccia eth1r2 = new Interfaccia("eth1", "2");
        Interfaccia eth2r2 = new Interfaccia("eth2", "3");
        Interfaccia eth1r3 = new Interfaccia("eth1", "4");
        Interfaccia eth1r4 = new Interfaccia("eth1", "5");

        // aggiunta interfacce a router
        r1.aggiungiInterfaccia(eth1r1);
        r1.aggiungiInterfaccia(eth2r1);
        r1.aggiungiInterfaccia(eth3r1);
        r2.aggiungiInterfaccia(eth1r2);
        r2.aggiungiInterfaccia(eth2r2);
        r3.aggiungiInterfaccia(eth1r3);
        r4.aggiungiInterfaccia(eth1r4);

        // creazione link
        Collegamento link1 = new Collegamento(eth1r1, eth1r2, 2);
        Collegamento link2 = new Collegamento(eth1r3, eth2r1, 1);
        Collegamento link3 = new Collegamento(eth1r4, eth3r1, 1);

        mainModel.addCollegamento(link1);
        mainModel.addCollegamento(link2);
        mainModel.addCollegamento(link3);

        // impostazione link alle interfacce
        eth1r1.setCollegamento(link1);
        eth1r2.setCollegamento(link1);
        eth1r3.setCollegamento(link2);
        eth2r1.setCollegamento(link2);
        eth3r1.setCollegamento(link3);
        eth1r4.setCollegamento(link3);

        mainModel.addRouter(r1);
        mainModel.addRouter(r2);
        mainModel.addRouter(r3);
        mainModel.addRouter(r4);

        mainController.disegnaRouter();
        mainController.disegnaCollegamenti();

        r1.start();
        r2.start();
        r3.start();
        r4.start();
    }

    /*
    public boolean apriCaricoScarico(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/Magazzino/View/DialogoCaricoScarico.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Carico/Scarico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.setMinHeight(280);
            dialogStage.setMinWidth(295);
            dialogStage.setMaxHeight(280);
            dialogStage.setMaxWidth(295);

            CaricaScaricaController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setArticolo();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            if(controller.isOkClicked()){
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
     */


    public MainModel getMainModel() {
        return mainModel;
    }

    public MainController getMainController() {
        return mainController;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void chiudi(){
        javafx.application.Platform.exit();
    }

}
