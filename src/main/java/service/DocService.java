package service;

import model.Education;
import model.UpStepDocModel;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import util.Status;

import java.util.List;

import static service.FileService.getDocumentTemplate;
import static service.FileService.saveNewDoc;
import static util.MessageTemplatesStorage.log;
import static util.MessageTemplatesStorage.logWarning;

public class DocService {

    public void createDocFile(UpStepDocModel docModel) {
//        try {
        XWPFDocument docTemplate = getDocumentTemplate();
//            String documentText = docTemplate.getDocument().toString();
/*            setMainInfo(docTemplate, docModel);
            setWorkTable(docTemplate, docModel);
            setEducationTable(docTemplate, docModel);
            setAdditionCourses(docTemplate, docModel);*/
        if (docTemplate != null) {
            fillDocTemplate(docTemplate, docModel);
            saveNewDoc(docTemplate, docModel.getDocModel().getName());
            Status.status = Status.StatusValue.SUCCESS;
            log("Файл успешно создан");
        } else {
            Status.status = Status.StatusValue.FILE_WAS_NOT_CREATION;
            logWarning("Файл не был создан");
        }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }

    }

    private void fillDocTemplate(XWPFDocument docTemplate, UpStepDocModel docModel) {
        setMainInfo(docTemplate, docModel);
        setWorkTable(docTemplate, docModel);
        setEducationTable(docTemplate, docModel);
        setAdditionCourses(docTemplate, docModel);
    }

    private void setMainInfo(XWPFDocument docTemplate, UpStepDocModel docModel) {
        List<XWPFParagraph> paragraphList = docTemplate.getParagraphs();
        for (String templateKey : docModel.getDocumentMap().keySet()) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String runText = run.getText(0);
                    if (runText != null && runText.contains(templateKey)) {
                        String value = docModel.getDocumentMap().get(templateKey);
                        String newText = "";
                        if (value != null) {
                            newText = runText.replace(templateKey, value);
                        }
                        run.setText(newText, 0);
                        break;
                    }
                }
            }
        }
    }

    private void setWorkTable(XWPFDocument docTemplate, UpStepDocModel docModel) {
        XWPFTable workTable = docTemplate.getTableArray(0);
        //Добавим строки
        for (int i = 0; i < docModel.getDocModel().getWorkPlaceList().size(); i++) {
            XWPFTableRow newRow = workTable.createRow();
            //Заполнение первой ячейки
            String timePeriod = docModel.getDocModel().getWorkPlaceList().get(i).getTimePeriod();
            String timeTotal = docModel.getDocModel().getWorkPlaceList().get(i).getTimeTotal();
            newRow.getCell(0).addParagraph();
            List<XWPFParagraph> paragraphListFirstCell = newRow.getCell(0).getParagraphs();
            paragraphListFirstCell.get(0).createRun().setText(timePeriod);
            paragraphListFirstCell.get(1).createRun().setText(timeTotal);

            //Заполнение второй
            String company = docModel.getDocModel().getWorkPlaceList().get(i).getCompany();
            String position = docModel.getDocModel().getWorkPlaceList().get(i).getPosition();
            String positionDescription = docModel.getDocModel().getWorkPlaceList().get(i).getDescription();
            newRow.getCell(1).addParagraph();
            newRow.getCell(1).addParagraph();
            newRow.getCell(1).addParagraph();
            List<XWPFParagraph> paragraphListSecondCell = newRow.getCell(1).getParagraphs();
            XWPFRun runCompany = paragraphListSecondCell.get(0).createRun();
            runCompany.setFontSize(16);
            runCompany.setText(company);
            paragraphListSecondCell.get(0).setSpacingBetween(1.43); //отступ после строки

            XWPFRun runPosition = paragraphListSecondCell.get(1).createRun();
            runPosition.setBold(true);
            runPosition.setText(position);
            runPosition.setFontSize(12);
            paragraphListSecondCell.get(1).setSpacingBetween(1.43); //отступ после строки

            XWPFRun runWorkResponsibilities = paragraphListSecondCell.get(2).createRun();
            runWorkResponsibilities.setFontFamily("Arial");
            runWorkResponsibilities.setItalic(true);
            runWorkResponsibilities.setFontSize(11);
            runWorkResponsibilities.setText("Обязанности:");

            paragraphListSecondCell.get(3).createRun().setText(positionDescription);

        }
        workTable.removeRow(0);
    }

    private void setEducationTable(XWPFDocument docTemplate, UpStepDocModel docModel) {
        XWPFTable educationTable = docTemplate.getTableArray(1);
        //Добавим строки
        for (int i = 0; i < docModel.getDocModel().getEducationList().size(); i++) {
            XWPFTableRow newRow = educationTable.createRow();
            //Заполнение первой ячейки
            String timePeriod = docModel.getDocModel().getEducationList().get(i).getTimePeriod();
            List<XWPFParagraph> paragraphListFirstCell = newRow.getCell(0).getParagraphs();
            paragraphListFirstCell.get(0).createRun().setText(timePeriod);

            //Заполнение второй
            String institution = docModel.getDocModel().getEducationList().get(i).getInstitution();
            String description = docModel.getDocModel().getEducationList().get(i).getDescription();
            newRow.getCell(1).addParagraph();
            List<XWPFParagraph> paragraphListSecondCell = newRow.getCell(1).getParagraphs();
            XWPFRun runInstitution = paragraphListSecondCell.get(0).createRun();
            runInstitution.setBold(true);
            runInstitution.setFontSize(12);
            runInstitution.setText(institution);
            paragraphListSecondCell.get(0).setSpacingBetween(1.43); //отступ после строки

            paragraphListSecondCell.get(1).createRun().setText(description);

        }
        educationTable.removeRow(0);
    }

    private void setAdditionCourses(XWPFDocument document, UpStepDocModel template) {
        if (template.getDocModel().getAdditionCourses() != null && template.getDocModel().getAdditionCourses().size() > 0) {
            for (Education course : template.getDocModel().getAdditionCourses()) {
                XWPFParagraph paragraphNew = document.createParagraph();
                paragraphNew.setFirstLineIndent(-850);
                paragraphNew.createRun().setText(course.getInstitution() + ", " + course.getDescription() + ", " + course.getTimePeriod());
            }
        }
    }








    private static CTP createHeaderModel(String headerContent) {
        // создаем хедер или верхний колонтитул
        CTP ctpHeaderModel = CTP.Factory.newInstance();
        CTR ctrHeaderModel = ctpHeaderModel.addNewR();
        CTText cttHeader = ctrHeaderModel.addNewT();

        cttHeader.setStringValue(headerContent);
        return ctpHeaderModel;
    }


}
