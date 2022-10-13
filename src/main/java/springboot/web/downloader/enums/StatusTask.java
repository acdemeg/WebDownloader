package springboot.web.downloader.enums;

/**
 * This class represent statuses set which
 * could be assigned on Task
 */
public enum StatusTask {
    DONE("DONE", "ВЫПОЛНЕНО"),
    ERROR("ERROR","ОШИБКА"),
    RUNNING("RUNNING","В ОБРАБОТКЕ"),
    UNDEFINED("UNDEFINED","НЕОПРЕДЕЛЕНО");

    private final String Eng;
    private final String Rus;
    public static final String Default = "Eng";

    StatusTask(final String Eng, final String Rus){
        this.Eng = Eng;
        this.Rus = Rus;
    }

    public String getStatus(final String lang){
        return (lang.equals("Eng")) ? this.Eng : this.Rus;
    }
}
