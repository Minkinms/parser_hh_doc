package service;

import javafx.scene.control.Alert;

public class UIMessagesStorage {

    public static void showErrorConvert(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка при конвертировании резюме");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.show();
    }

    public static void showErrorCreation(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка при формировании файла");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.show();
    }
}
