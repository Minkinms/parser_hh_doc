package service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static util.MessageTemplatesStorage.log;
import static util.MessageTemplatesStorage.logError;

public class FileService {

    private static final String MAIN_FILE_PATH = "C:/docFiles/";  //TODO сделать поиск в текущей папке. Перенести в класс шаблона
    private static final String TEMP_FILE_PATH = "C:/docFiles/temp.docx";  //TODO сделать поиск в текущей папке. Перенести в класс шаблона
    private static final String SAVE_FILE_PATH = "C:/docFiles/%s UpStep.docx";    //TODO сделать сохранение в текущей папке

    /**
     * Метод для получения перечня файлов в главной папке приложения
     * @return список всех файлов в главной папке
     */
    public static List<String> getFileList() {
        File folder = new File(MAIN_FILE_PATH);
        File[] files = folder.listFiles();          //TODO добавить проверки null
        List<String> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    /**
     * Метод для получения модели документа из файла шаблона
     *
     * @return модель документа
     */
    public static XWPFDocument getDocumentTemplate() {
        try (FileInputStream fis = new FileInputStream(TEMP_FILE_PATH)) {
            return new XWPFDocument(fis);
        } catch (Exception exception) {
            logError("Ошибка при чтении шаблона резюме", exception);
            UIMessagesStorage.showErrorCreation("Ошибка при чтении файла шаблона", "Не удалось найти файл шаблона");
            return null;
        }
    }

    /**
     * Метод для получения модели документа из файла
     *
     * @param fileName имя файла
     * @return модель документа
     */
    public static XWPFDocument getDocumentFromFile(String fileName) {
        String fullPath = MAIN_FILE_PATH + fileName;
        log("Открывается документ: " + fullPath);
        try (FileInputStream fis = new FileInputStream(fullPath)) {
            return new XWPFDocument(fis);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new AssertionError("Ошибка при чтении исходного файла резюме");
        }
    }

    /**
     * Метод для сохранения нового файла
     *
     * @param document модель документа для сохранения
     * @param fileName имя нового файла
     */
    public static void saveNewDoc(XWPFDocument document, String fileName) {
        String saveFilePath = String.format(SAVE_FILE_PATH, fileName);
        log("Сохранение нового файла: " + saveFilePath);
        try (FileOutputStream outputStream = new FileOutputStream(saveFilePath)) {
            document.write(outputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new AssertionError("Ошибка при сохранении файла в: '" + saveFilePath + "'");
        }
    }
}
