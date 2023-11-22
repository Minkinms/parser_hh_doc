package util;

public class Status {

    public static StatusValue status = StatusValue.NO_DATA;

    public enum StatusValue {
        NO_DATA("Нет данных"),
        SUCCESS("Файл успешно создан"),
        FILE_WAS_NOT_CREATION("!!! Файл не был создан !!!")
        ;

        private String value;

        StatusValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
