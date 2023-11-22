package service;

import model.DocModel;
import model.HeadHunterCVModel;
import model.UpStepDocModel;


public class Parser {

    public void parseDocxToDocx(String fileName) {
        HeadHunterCVModel headHunterCVmodel = new HeadHunterCVService().getModelFromDocxFile(fileName);
        DocModel docModel = ConvertService.convertHHtoDoc(headHunterCVmodel);
        UpStepDocModel template = new UpStepDocModel(docModel);
        new DocService().createDocFile(template);
    }

}
