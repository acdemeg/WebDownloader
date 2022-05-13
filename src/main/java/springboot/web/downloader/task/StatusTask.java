package springboot.web.downloader.task;

public enum StatusTask {
    DONE("Done"),
    ERROR("Error");

    private final String value;

     StatusTask(String value) {
         this.value = value;
     }

    public String value() { return this.value; }

}
