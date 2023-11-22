package model;

import lombok.*;

import java.util.*;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class HeadHunterCVModel {
    private String name;
    private String Birthday;
    private String age;
    private String address;
    private String email;
    private String phone;
    private String skypeTelegram;
    private String workExperience;
    private List<WorkPlace> workPlaceList;
    private List<Education> educationList;
    private List<Education> additionCourses;
    private Map<String, Integer> tableHeaders = new HashMap<>();
    private Map<TableHeaders, HeaderPosition> headerMap = new HashMap<>();

    public enum TableHeaders {
        //Заголовки после раздела опыта работы
        EDUCATION("Образование"),
        ADDITION_COURSES("Повышение квалификации, курсы"),
        TESTS_EXAMS("Тесты, экзамены"),
        EL_CERTIFICATES("Электронные сертификаты"),
        SKILLS("Ключевые навыки"),
        DRIVER_EXPERIENCE("Опыт вождения"),
        ADDITION_INFO("Дополнительная информация"),
        ;

        private final String header;

        TableHeaders(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }
}
