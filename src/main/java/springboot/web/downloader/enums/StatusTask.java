package springboot.web.downloader.enums;

/**
 * This class represent status of Task
 */
public enum StatusTask {
    DONE("DONE", "ВЫПОЛНЕНО"),
    ERROR("ERROR", "ОШИБКА"),
    RUNNING("RUNNING", "В ОБРАБОТКЕ"),
    UNDEFINED("UNDEFINED", "НЕОПРЕДЕЛЕНО");

    private final String eng;
    private final String rus;

    StatusTask(final String Eng, final String Rus) {
        this.eng = Eng;
        this.rus = Rus;
    }

    public String getStatus(final String lang) {
        return (lang.equals("Eng")) ? this.eng : this.rus;
    }
}
