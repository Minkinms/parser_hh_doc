package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import service.Parser;
import util.MessageTemplatesStorage;
import util.Status;

import java.util.List;

import static service.FileService.getFileList;

public class MainWindow {

    public void setMainWindowProperties(Stage stage) {
        GridPane mainGridPane = getMainGridPane();
        fillMainGridPane(mainGridPane);
        Scene scene = new Scene(mainGridPane);
        setMainWindowProperties(stage, scene);
    }

    /**
     * Метод для установки основных параметров окна
     */
    private void setMainWindowProperties(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setTitle("Преобразование резюме из формата HH в UpStep");
        stage.setWidth(530);
        stage.setHeight(500);
    }

    /**
     * Метод для создания сетки расположения элементов в главном окне
     */
    private GridPane getMainGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        //Отступы с каждой стороны
        gridPane.setPadding(new Insets(10, 20, 40, 20));
        //Расстояние между столбцами. Горизонтальное и вертикальное
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        //Отображение сетки
//        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        //Столбцы
        ColumnConstraints firstColumn = new ColumnConstraints(100, 270, Double.MAX_VALUE);
        firstColumn.setHalignment(HPos.CENTER);

        ColumnConstraints secondColumn = new ColumnConstraints(200,200, Double.MAX_VALUE);
        secondColumn.setHalignment(HPos.LEFT);

        gridPane.getColumnConstraints().addAll(firstColumn, secondColumn);

        return gridPane;
    }

    /**
     * Метод для наполнения сетки элементами
     */
    private void fillMainGridPane(GridPane gridPane) {
        //Заголовок списка
        addLabel(gridPane, "Список файлов в папке 'C:/docFiles'", 0, 0);

        //Список файлов в папке
        List<String> CVList = getFileList();
        ObservableList<String> CVListForView = FXCollections.observableArrayList(CVList);
        ListView<String> langsListView = new ListView<String>(CVListForView);
        langsListView.setMinWidth(250);
        gridPane.add(langsListView, 0, 1);

        Button convertBtn = addButton(gridPane, "Преобразовать", 1, 1);
        GridPane.setValignment(convertBtn, VPos.TOP);
        GridPane.setHalignment(convertBtn, HPos.CENTER);

        Button refreshBtn = addButton(gridPane, "Обновить список", 0, 2);
        GridPane.setValignment(refreshBtn, VPos.TOP);
        GridPane.setHalignment(refreshBtn, HPos.LEFT);

        //Индикатор статуса преобразования
        addLabel(gridPane, "Статус : ", 0, 3);
        Label statusLabel = addLabel(gridPane, Status.status.getValue(), 0, 4);

        //Установка действий кнопкам
        setActionConvertBtn(convertBtn, langsListView, statusLabel);
        setActionRefreshBtn(refreshBtn, langsListView);

    }

    //TODO Вынести методы в класс (отдельные классы) сервиса конструктора элементов

    private void addHeaderLabel(GridPane gridPane, String text ) {
        Label headerLabel = new Label(text);
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
    }

    private Label addLabel(GridPane gridPane, String text, int column, int row) {
        Label linkLabel = new Label(text);
        gridPane.add(linkLabel, column,row);
        GridPane.setValignment(linkLabel, VPos.TOP);
        GridPane.setHalignment(linkLabel, HPos.LEFT);
        return linkLabel;
    }

    private TextField addTextField(GridPane gridPane, int column, int row) {
        TextField textField = new TextField();
        textField.setPrefHeight(40);
        gridPane.add(textField, column,row);
        return textField;
    }

    private Button addButton(GridPane gridPane, String text, int column, int row) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        button.setDefaultButton(true);
        button.setPrefWidth(200);
        gridPane.add(button, column, row);
        GridPane.setMargin(button, new Insets(5, 0,5,0));
        return button;
    }

    private void setActionConvertBtn(Button button, ListView<String> langsListView, Label statusLabel) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageTemplatesStorage.log("click on button 'Convert'");
                String selectedName = langsListView.getSelectionModel().getSelectedItem();
                MessageTemplatesStorage.log("Parse file: '" + selectedName + "'");
                new Parser().parseDocxToDocx(selectedName);
                statusLabel.setText(Status.status.getValue());
            }
        });
    }

    private void setActionRefreshBtn(Button button, ListView<String> langsListView) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageTemplatesStorage.log("click on button 'Refresh'");
                List<String> CVList = getFileList();
                ObservableList<String> CVListForView = FXCollections.observableArrayList(CVList);
                langsListView.setItems(CVListForView);
            }
        });
    }

}
