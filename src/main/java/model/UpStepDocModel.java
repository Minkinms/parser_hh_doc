package model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UpStepDocModel {

    private Map<String, String> documentMap = new HashMap<>();
    private DocModel docModel;

    public UpStepDocModel(DocModel docModel) {
        this.docModel = docModel;
        fillTemplate();
    }

    private void fillTemplate() {
        documentMap.put("{resume-personal-name}", docModel.getName());
        documentMap.put("{resume-personal-birthday}", docModel.getBirthday());
        documentMap.put("{age}", docModel.getAge());
        documentMap.put("{address}", docModel.getAddress());
        documentMap.put("{email}", docModel.getEmail());
        documentMap.put("{phone}", docModel.getPhone());
        documentMap.put("{skypeTelegram}", docModel.getSkypeTelegram());
        documentMap.put("{workExperience}", docModel.getWorkExperience());
    }


}
