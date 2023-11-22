package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class DocModel {

    private String name;
    private String birthday;
    private String age;
    private String address;
    private String email;
    private String phone;
    private String skypeTelegram;
    private String workExperience;
    private List<WorkPlace> workPlaceList = new ArrayList<>();
    private List<Education> educationList = new ArrayList<>();
    private List<Education> additionCourses = new ArrayList<>();
}
