package springboot.web.downloader.enums;

import lombok.RequiredArgsConstructor;

/**
 * This class represent error messages
 */
@RequiredArgsConstructor
public enum ErrorMessage {
    TASK_NOT_FOUND("Task not found", "Задача не найдена"),
    INTERNAL_SERVER_ERROR("Internal server error", "Ошибка сервера"),
    PRECONDITION_FAILED("Precondition failed", "Предварительное условие не выполнено"),
    FILE_NOT_FOUND("File not found", "Файл не найден");

    private final String eng;
    private final String rus;

    public String getMessage(final String lang) {
        return (lang.equals("Eng")) ? this.eng : this.rus;
    }
}
