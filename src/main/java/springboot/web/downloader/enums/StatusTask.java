package springboot.web.downloader.enums;

import lombok.RequiredArgsConstructor;

/**
 * This class represent status of Task
 */
@RequiredArgsConstructor
public enum StatusTask {
    DONE("DONE", "ВЫПОЛНЕНО"),
    ERROR("ERROR", "ОШИБКА"),
    RUNNING("RUNNING", "В ОБРАБОТКЕ"),
    UNDEFINED("UNDEFINED", "НЕОПРЕДЕЛЕНО");

    private final String eng;
    private final String rus;

    public String getStatus(final String lang) {
        return (lang.equals("Eng")) ? this.eng : this.rus;
    }
}
