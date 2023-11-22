package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class WorkPlace {
    private String timePeriod;
    private String timeTotal;
    private String company;
    private String position;
    private String description;
}
