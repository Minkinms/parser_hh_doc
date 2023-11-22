package service;

import model.DocModel;
import model.HeadHunterCVModel;

public class ConvertService {

    public static DocModel convertHHtoDoc(HeadHunterCVModel hhModel) {
        DocModel docModel = new DocModel();
        docModel.setName(hhModel.getName());
        docModel.setBirthday(hhModel.getBirthday());
        docModel.setAge(hhModel.getAge());
        docModel.setAddress(hhModel.getAddress());
        docModel.setEmail(hhModel.getEmail());
        docModel.setPhone(hhModel.getPhone());
        docModel.setSkypeTelegram(hhModel.getSkypeTelegram());
        docModel.setWorkExperience(hhModel.getWorkExperience());
        docModel.setWorkPlaceList(hhModel.getWorkPlaceList());
        docModel.setEducationList(hhModel.getEducationList());
        docModel.setAdditionCourses(hhModel.getAdditionCourses());



        return docModel;
    }
}
