package springboot.web.downloader.enums;

/**
 * This class represent error messages
 */
public enum ErrorMessage {
    TASK_NOT_FOUND("Task not found", "Задача не найдена"),
    INTERNAL_SERVER_ERROR("Internal server error", "Ошибка сервера"),
    PRECONDITION_FAILED("Precondition failed", "Предварительное условие не выполнено"),
    FILE_NOT_FOUND("File not found", "Файл не найден");

    private final String eng;
    private final String rus;

    ErrorMessage(final String Eng, final String Rus){
        this.eng = Eng;
        this.rus = Rus;
    }

    public String getMessage(final String lang){
        return (lang.equals("Eng")) ? this.eng : this.rus;
    }
}
