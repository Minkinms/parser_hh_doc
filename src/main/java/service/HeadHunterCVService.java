package service;

import model.Education;
import model.HeadHunterCVModel;
import model.HeadHunterCVModel.TableHeaders;
import model.HeaderPosition;
import model.WorkPlace;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import page.HeadHunterPage;
import util.MessageTemplatesStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.HeadHunterCVModel.TableHeaders.*;
import static service.FileService.getDocumentFromFile;
import static util.MessageTemplatesStorage.log;

public class HeadHunterCVService {

    //Для парсинга веб страницы
    //TODO пока не используется, но возможно в дальнейшем
    public HeadHunterCVModel getModelFromWeb() {
        String taskLink = new LinkService().getCVfromFile();
        SeleniumSteps seleniumSteps = new SeleniumSteps();
        seleniumSteps.openLink(taskLink);
        HeadHunterCVModel model = parseWebPage();
        seleniumSteps.closeDriver();
        return model;
    }

    //Для парсинга файла '.docx'

    /**
     * Метод для преобразования файла в модель данных HH
     *
     * @param fileName имя файла
     * @return модель данных HH
     */
    public HeadHunterCVModel getModelFromDocxFile(String fileName) {
        HeadHunterCVModel model = new HeadHunterCVModel();
        XWPFTable mainTable = getDocumentFromFile(fileName).getTableArray(0);

        findTableHeaders(model, mainTable);
        parseMainInform(model, mainTable);
        model.setWorkExperience(readWorkExperience(mainTable));
        parseWorkPlaces(model, mainTable);
        parseEducation(model, mainTable);
        parseAdditionCourses(model, mainTable);

        return model;
    }

    private void findTableHeaders(HeadHunterCVModel model, XWPFTable table) {
        log("Анализ заголовков");
//        model.getTableHeaders().put(EDUCATION.getHeader(), findRow(EDUCATION.getHeader(), table));
//        model.getTableHeaders().put(ADDITION_COURSES.getHeader(), findRow(ADDITION_COURSES.getHeader(), table));
//        model.getTableHeaders().put(SKILLS.getHeader(), findRow(SKILLS.getHeader(), table));
        int actualPosition = 1;
        for(TableHeaders tableHeader : TableHeaders.values()) {
            int row = findRow(tableHeader.getHeader(), table);
            if(row != 0) {
                model.getHeaderMap().put(tableHeader, new HeaderPosition(actualPosition, row));
                actualPosition++;
            }
        }
    }

    //TODO Может не быть фотографии
    //TODO Обработать отдельно. Сейчас одна ошибка не позволит проверить другие
    private void parseMainInform(HeadHunterCVModel model, XWPFTable table) {
        try {
            List<XWPFParagraph> paragraphs;
            if (table.getRow(0).getTableCells().size() == 1) {
                System.out.println("В резюме нет фотографии");
                paragraphs = table.getRow(0).getCell(0).getParagraphs();
            } else {
                paragraphs = table.getRow(0).getCell(1).getParagraphs();
            }
            model.setName(readName(paragraphs));
            model.setPhone(readPhone(paragraphs));
            model.setEmail(readEmail(paragraphs));
            model.setBirthday(readBirthday(paragraphs));
            model.setAge(readAge(paragraphs));
            model.setAddress(readAddress(paragraphs));
        } catch (Exception exception) {
            UIMessagesStorage.showErrorConvert("Часть информации будет отсутствовать", "Ошибка при считывании основной информации");
        }
    }

    private String readWorkExperience(XWPFTable table) {
        try {
            return table.getRow(3).getCell(0).getText().trim().substring(13);
        } catch (Exception exception) {
            UIMessagesStorage.showErrorConvert("Часть информации будет отсутствовать", "Ошибка при считывании опыта работы");
            return "!!! Вписать вручную !!!";
        }
    }

    private void parseWorkPlaces(HeadHunterCVModel model, XWPFTable table) {
        try {
            List<WorkPlace> workPlaceList = new ArrayList<>();
            List<XWPFTableRow> rows = table.getRows();
            for (int i = 4; i < rows.size(); i++) {
                String rowText = rows.get(i).getCell(0).getText();
                if (rowText.equals(EDUCATION.getHeader())) {
                    break;
                }
                workPlaceList.add(readWorkPlace(rows.get(i)));
            }
            model.setWorkPlaceList(workPlaceList);
        } catch (Exception exception) {
            UIMessagesStorage.showErrorConvert("Часть информации будет отсутствовать", "Ошибка при считывании мест работы");
        }
    }

    private void parseEducation(HeadHunterCVModel model, XWPFTable table) {
        try {
//            int coursesRowsFrom = model.getTableHeaders().get(EDUCATION.getHeader()) + 2;
            int coursesRowsFrom = model.getHeaderMap().get(EDUCATION).getRow() + 2; //После мест работы всегда идет Образование
            int coursesRowsTo = getRowsTo(model, EDUCATION);
/*            if (model.getTableHeaders().get(ADDITION_COURSES.getHeader()) != 0) {
                coursesRowsTo = model.getTableHeaders().get(ADDITION_COURSES.getHeader());
            } else {
                if (model.getTableHeaders().get(SKILLS.getHeader()) != 0) {
                    coursesRowsTo = model.getTableHeaders().get(SKILLS.getHeader());
                }
            }*/
            model.setEducationList(getEducationList(table, coursesRowsFrom, coursesRowsTo));
        } catch (Exception exception) {
            model.setEducationList(new ArrayList<>());
            UIMessagesStorage.showErrorConvert("Часть информации будет отсутствовать", "Ошибка при считывании мест обучения");
        }
    }

    private int getRowsTo(HeadHunterCVModel model, TableHeaders header) {
        Map<TableHeaders, HeaderPosition> headersModelMap = model.getHeaderMap();
        int headerPosition = headersModelMap.get(header).getActualPosition();

        for(TableHeaders headerFromModel : headersModelMap.keySet()) {
            int actualPosition = headersModelMap.get(headerFromModel).getActualPosition();
            if(actualPosition == headerPosition + 1) {
                return headersModelMap.get(headerFromModel).getRow();
            }
        }
        return 0;
    }

    private void parseAdditionCourses(HeadHunterCVModel model, XWPFTable table) {
        Map<TableHeaders, HeaderPosition> headersModelMap = model.getHeaderMap();
        try {
            if(headersModelMap.containsKey(ADDITION_COURSES)) {
                int coursesRowsFrom = headersModelMap.get(ADDITION_COURSES).getRow() + 1;
                int coursesRowsTo = getRowsTo(model, ADDITION_COURSES);
                model.setAdditionCourses(getEducationList(table, coursesRowsFrom, coursesRowsTo));
            }

/*            if (model.getTableHeaders().get(ADDITION_COURSES.getHeader()) != 0) {
                int coursesRowsFrom = findRow(ADDITION_COURSES.getHeader(), table) + 1;
                int coursesRowsTo = findRow(SKILLS.getHeader(), table);
                model.setAdditionCourses(getEducationList(table, coursesRowsFrom, coursesRowsTo));
            }*/
        } catch (Exception exception) {
            model.setAdditionCourses(new ArrayList<>());
            MessageTemplatesStorage.logError("Ошибка при считывании мест дополнительного обучения", exception);
            UIMessagesStorage.showErrorConvert("Часть информации будет отсутствовать", "Ошибка при считывании мест дополнительного обучения");
        }

    }

    private List<Education> getEducationList(XWPFTable table, int rowsFrom, int rowsTo) {
        List<Education> educationList = new ArrayList<>();
        List<XWPFTableRow> rows = table.getRows();
        for (int i = rowsFrom; i < rowsTo; i++) {
            educationList.add(readEducation(rows.get(i)));
        }
        return educationList;
    }

    private int findRow(String value, XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        int rowPosition = 0;
        for (int i = 0; i < rows.size(); i++) {
            String rowText = rows.get(i).getCell(0).getText();
            if (rowText.equals(value)) {
                rowPosition = i;
                break;
            }
        }
        return rowPosition;
    }

    private Education readEducation(XWPFTableRow row) {
        String timePeriod = row.getCell(0).getParagraphs().get(0).getText();
        List<XWPFParagraph> paragraphs = row.getCell(1).getParagraphs();
        String institution = getParagraphText(paragraphs, 0);
        String description = getParagraphText(paragraphs, 1);
        return new Education(timePeriod, institution, description);
    }

    private WorkPlace readWorkPlace(XWPFTableRow row) {
        String timeCell = row.getCell(0).getParagraphs().get(0).getText();
        String timePeriod = timeCell.split("\n")[0];
        String timeTotal = timeCell.split("\n")[1];
        List<XWPFParagraph> paragraphs = row.getCell(2).getParagraphs();
        String workName = getParagraphText(paragraphs, 0);
        String position = getParagraphText(paragraphs, paragraphs.size() - 2);
        String positionDescription = getParagraphText(paragraphs, paragraphs.size() - 1);
        return new WorkPlace(timePeriod, timeTotal, workName, position, positionDescription);
    }

    private String readName(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 0);
    }

    private String readPhone(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 3);
    }

    private String readEmail(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 4);
    }

    private String readAge(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 1).split(",")[1].trim();
    }

    private String readBirthday(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 1).split(",")[2].trim().substring(7);  //TODO изменить месяц. Также прихватывает лишние знаки для женщин
    }

    private String readAddress(List<XWPFParagraph> paragraphs) {
        return getParagraphText(paragraphs, 6).split(":")[1];
    }

    private String getParagraphText(List<XWPFParagraph> paragraphs, int index) {
        return paragraphs.get(index).getText();
    }


    private HeadHunterCVModel parseWebPage() {
        HeadHunterCVModel model = new HeadHunterCVModel();
        HeadHunterPage page = new HeadHunterPage();
        model.setName(page.getName());
        model.setBirthday(page.getBirthday());  //TODO изменить месяц
        model.setAge(page.getAge());    //TODO  оставить цифры или сформировать строку с год/лет
        model.setAddress(page.getAddress());    //локация
        model.setEmail(page.getEmail());
        model.setPhone(page.getPhone());


        return model;
    }

}
