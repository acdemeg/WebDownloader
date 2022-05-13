package springboot.web.downloader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TestUtils {

    private final static String sitesPath = System.getProperty("user.home") + "/test/sites/";
    private final static String archivedPath = System.getProperty("user.home") + "/test/archived/";

    public static void setFinalStaticField(String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = WebDownloader.class.getField(fieldName);
        field.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(field, value);
    }

    public static void prepareTestEnv() throws IOException, NoSuchFieldException, IllegalAccessException {
        FileUtils.forceMkdir(new File(sitesPath));
        FileUtils.forceMkdir(new File(archivedPath));
        setFinalStaticField("baseSites", sitesPath);
        setFinalStaticField("baseArchived", archivedPath);
    }

    public static void discardTestEnv() throws IOException {
        FileUtils.cleanDirectory(new File(sitesPath));
        FileUtils.cleanDirectory(new File(archivedPath));
    }
}
