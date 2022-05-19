package Routing.Controller;

import Routing.MainApp;
import Routing.Model.*;
import Routing.Model.Pacchetti.Pacchetto;
import Routing.Model.Pacchetti.TipoPacchetto;
import Routing.View.DragResizeMod;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    private MainApp mainApp;
    private MainModel mainModel;

    private boolean staEseguendoCollegamento = false;
    private Router primoRouter;
    private Router secondoRouter;

    @FXML
    private Group group;
    @FXML
    private TextArea logTextArea;
    @FXML
    private ChoiceBox<Router> routerSorgenteChoiceBox;
    @FXML
    private ChoiceBox<Router> routerDestinazioneChoiceBox;
    @FXML
    private ChoiceBox<Router> tabellaRouterChoiceBox;
    @FXML
    private TableView<Rotta> tabellaRoutingTableView;
    @FXML
    private TableColumn<Rotta, String> destinazioneTableColumn;
    @FXML
    private TableColumn<Rotta, String> interfacciaTableColumn;
    @FXML
    private TableColumn<Rotta, String> costoTableColumn;
    @FXML
    private TableView<Router> listHopPacchetto;
    @FXML
    private TableColumn<Router, String> hopTableColumn;
    @FXML
    private ChoiceBox<TipoPacchetto> tipoPacchettoChoiceBox;
    @FXML
    private ChoiceBox<Router> impostazioniRouterChoiceBox;
    @FXML
    private TextArea interfacceRouterTextArea;
    @FXML
    private TextArea contenutoPacchettoTextArea;
    @FXML
    private Button collegaRouterButton;
    @FXML
    private Button interrompiSimulazioneButton;

    public MainController(){
        DragResizeMod.setMainController(this);
    }

    @FXML
    private void initialize(){

        interrompiSimulazioneButton.setDisable(true);

        tipoPacchettoChoiceBox.getItems().setAll(TipoPacchetto.values());

        destinazioneTableColumn.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getRouter().toString()));
        interfacciaTableColumn.setCellValueFactory((a -> new SimpleStringProperty(a.getValue().getInterfaccia().getLabel() + " ("+a.getValue().getInterfaccia().getCollegamento().getAltroNodo(a.getValue().getInterfaccia()).getRouter().getLabel()+")")));
        costoTableColumn.setCellValueFactory((a -> new SimpleStringProperty(""+a.getValue().getCosto())));

        hopTableColumn.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().toString()));

        Timer timer = new Timer();

        // Task aggiornamento tableview tabella routing ogni secondo
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> tabellaRoutingTableView.refresh());
            }
        }, new Date(), 1000);

        // Task aggiornamento tableview rotta pacchetto ogni secondo
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> listHopPacchetto.refresh());
            }
        }, new Date(), 1000);
    }


    /**
     * Aggiorna la tabella di routing quando viene selezionato un router dalla choicebox
     */
    @FXML
    public void selezionatoRouterPerTabella(){
        Router routerSelezionato = tabellaRouterChoiceBox.getValue();
        if(routerSelezionato!=null){
            try {
                tabellaRoutingTableView.setItems(routerSelezionato.getTabellaRouting().getTabellaObservableList());
                tabellaRoutingTableView.refresh();
            }catch(Exception e){
                System.out.println("Pass");
            }
        }
    }

    public void tableViewRefresh(){
        tabellaRoutingTableView.refresh();
    }

    private void postMainAppInitialize(){
        disegnaRouter();
        disegnaCollegamenti();
    }

    /**
     * Avvia la simulazione
     */
    public void avviaSimulazione(){
        mainApp.setSimulazioneAttiva(true);
        interrompiSimulazioneButton.setDisable(false);
        for(Router router : mainModel.getListaRouter()){
            router.start();
        }
    }

    /**
     * Impostazione router selezionato tramite choicebox in tab informazioni router
     */
    public void impostaRouter(){
        Router routerSelezionato = impostazioniRouterChoiceBox.getValue();
        interfacceRouterTextArea.clear();
        if(routerSelezionato != null) {
            for (Interfaccia interfaccia : routerSelezionato.getInterfacce()) {
                interfacceRouterTextArea.appendText("Interfaccia "+interfaccia+" collegata a "+interfaccia.getCollegamento().getAltroNodo(interfaccia)+"\n");
            }
        }
    }

    public boolean staEseguendoCollegamento() {
        return staEseguendoCollegamento;
    }

    public Router getPrimoRouter() {
        return primoRouter;
    }

    public void setPrimoRouter(Router primoRouter) {
        this.primoRouter = primoRouter;
    }

    public void setSecondoRouter(Router secondoRouter) {
        this.secondoRouter = secondoRouter;
    }

    /**
     * effettua il collegamento tra due router
     */
    public void terminaCollegamento(){
        if(primoRouter != secondoRouter) {
            Interfaccia interfacciaPrimoRouter = primoRouter.aggiungiInterfaccia();
            Interfaccia interfacciaSecondoRouter = secondoRouter.aggiungiInterfaccia();
            Collegamento collegamento = new Collegamento(interfacciaPrimoRouter, interfacciaSecondoRouter, 1);
            interfacciaPrimoRouter.setCollegamento(collegamento);
            interfacciaSecondoRouter.setCollegamento(collegamento);
            mainModel.addCollegamento(collegamento);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Impossibile collegare router a se stesso");
            alert.showAndWait();
        }
        disegnaCollegamenti();
        staEseguendoCollegamento = false;
        primoRouter = null;
        secondoRouter = null;
        collegaRouterButton.setDisable(false);

    }

    /**
     * Avvia la procedura di creazione del collegamento tra due router
     */
    public void collegaRouter(){
        staEseguendoCollegamento = true;
        collegaRouterButton.setDisable(true);
    }

    /**
     * Ottiene il router corrispondente al canvas
     * @param canvas istanza del canvas del router
     * @return istanza del router corrispondente al canvas
     */
    public Router getRouterFromCanvas(Canvas canvas){
        for(Router router : mainModel.getListaRouter()){
            if(router.getCanvas() == canvas){
                return router;
            }
        }
        return null;
    }

    /**
     * Eseguie il reset della simulazione
     */
    public void reset(){
        for(Router router : mainModel.getListaRouter()){
            router.reset();
        }
        mainApp.setSimulazioneAttiva(false);
        interrompiSimulazioneButton.setDisable(true);
    }

    /**
     * Aggiunge un nuovo router alla rete
     */
    public void aggiungiRouter(){
        Router router = new Router( this);
        mainModel.addRouter(router);
        disegnaRouter(router);
        disegnaCollegamenti();
        routerSorgenteChoiceBox.getItems().add(router);
        tabellaRouterChoiceBox.getItems().add(router);
        routerDestinazioneChoiceBox.getItems().add(router);
        impostazioniRouterChoiceBox.getItems().add(router);
        if(mainApp.isSimulazioneAttiva()){
            router.start();
        }
    }

    /**
     * Elimina le linee di collegamento
     * Funzione chiamata ovunque più volte perchè se chiamata solo una volta non funziona
     * non so il motivo ma funziona
     */
    public void eliminaCollegamenti(){
        for(int i=0; i<group.getChildren().size(); i++){
            Node node = group.getChildren().get(i);
            if(node instanceof Line){
                group.getChildren().remove(node);
            }
        }
    }

    /**
     * Disegna il canvas corrispondente ad un singolo router
     * @param router router da disegnare
     */
    public void disegnaRouter(Router router){
        Canvas canvas = new Canvas(100, 100);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(3);
        graphicsContext.setStroke(Color.BLACK);
        Font font = Font.font(15);
        graphicsContext.setFont(font);
        graphicsContext.fillText(router.toString(), 12, 90);
        graphicsContext.fillOval(30, 30, 40, 40);
        DragResizeMod.makeResizable(canvas);
        group.getChildren().add(canvas);
        canvas.setLayoutX(100);
        canvas.setLayoutY(100);
        router.setCanvas(canvas);

    }

    /**
     * Disegna i collegamenti presenti tra i router della rete
     */
    public void disegnaCollegamenti(){
        eliminaCollegamenti();
        eliminaCollegamenti();
        for(Collegamento collegamento : mainModel.getListaCollegamenti()){
            if(collegamento.collegamentoValido()) {
                Canvas canvasRouter1 = collegamento.getNodo1().getRouter().getCanvas();
                Canvas canvasRouter2 = collegamento.getNodo2().getRouter().getCanvas();
                Line line = new Line(canvasRouter1.getLayoutX() + 50, canvasRouter1.getLayoutY() + 50, canvasRouter2.getLayoutX() + 50, canvasRouter2.getLayoutY() + 50);
                line.setStrokeWidth(4);
                group.getChildren().add(line);
            }
        }
    }

    /**
     * disegna tutti i router presenti nella listaRouter di mainModel
     */
    public void disegnaRouter(){
        // Disegna i router su canvas
        int spawnPosX = 0;
        int spawnPosY = 0;
        for(Router router : mainModel.getListaRouter()){
            Canvas canvas = new Canvas(100, 100);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.setLineWidth(3);
            graphicsContext.setStroke(Color.BLACK);
            Font font = Font.font(15);
            graphicsContext.setFont(font);
            graphicsContext.fillText(router.toString(), 12, 90);
            graphicsContext.fillOval(30, 30, 40, 40);
            DragResizeMod.makeResizable(canvas);
            group.getChildren().add(canvas);
            canvas.setLayoutX(spawnPosX);
            canvas.setLayoutY(spawnPosY);
            spawnPosX+=100;
            if(spawnPosX>600){
                spawnPosX = 0;
                spawnPosY += 100;
            }
            router.setCanvas(canvas);
        }
        // Aggiunge router a choicebox
        tabellaRouterChoiceBox.getItems().addAll(mainModel.getListaRouter());
        routerDestinazioneChoiceBox.getItems().addAll(mainModel.getListaRouter());
        routerSorgenteChoiceBox.getItems().addAll(mainModel.getListaRouter());
        impostazioniRouterChoiceBox.getItems().addAll(mainModel.getListaRouter());
    }

    /**
     * Invia il pacchetto creato nella tab invio pacchetto
     */
    @FXML
    public void inviaPacchetto(){
        if(mainApp.isSimulazioneAttiva()) {
            Router sorgente = routerSorgenteChoiceBox.getValue();
            Router destinazione = routerDestinazioneChoiceBox.getValue();
            TipoPacchetto tipoPacchetto = tipoPacchettoChoiceBox.getValue();
            String contenuto = contenutoPacchettoTextArea.getText();
            if(sorgente!=null && destinazione!=null && tipoPacchetto!=null) {
                Pacchetto pacchetto = new Pacchetto(contenuto, sorgente, destinazione, tipoPacchetto);
                if (sorgente.getTabellaRouting().esisteRottaPerRouter(destinazione)) {
                    sorgente.inviaPacchetto(pacchetto);
                    listHopPacchetto.setItems(pacchetto.getRottaObservableList());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Rotta non esistente. Attendi la convergenza.");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Valori non validi");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Avvia la simulazione per inviare un pacchetto");
            alert.showAndWait();
        }
    }

    /**
     * Stampa su stdout e sulla tab di log dei messaggi
     * @param messaggio messaggio da aggiungere al log
     */
    public synchronized void log(String messaggio){
        System.out.print(messaggio);
        Platform.runLater(() -> logTextArea.appendText(messaggio));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainModel = mainApp.getMainModel();
        postMainAppInitialize();
    }
}
