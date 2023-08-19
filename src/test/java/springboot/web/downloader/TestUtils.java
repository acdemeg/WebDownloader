package springboot.web.downloader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TestUtils {

    private final static String SITES_PATH = System.getProperty("user.home") + "/test/sites/";
    private final static String ARCHIVED_PATH = System.getProperty("user.home") + "/test/archived/";
    private final static String SITEMAPS_PATH = System.getProperty("user.home") + "/test/sitemaps/";

    public static void setFinalStaticField(String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = WebDownloader.class.getField(fieldName);
        field.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(field, value);
    }

    public static void prepareTestEnv() throws IOException, NoSuchFieldException, IllegalAccessException {
        FileUtils.forceMkdir(new File(SITES_PATH));
        FileUtils.forceMkdir(new File(ARCHIVED_PATH));
        FileUtils.forceMkdir(new File(SITEMAPS_PATH));
        setFinalStaticField("BASE_SITES", SITES_PATH);
        setFinalStaticField("BASE_ARCHIVED", ARCHIVED_PATH);
        setFinalStaticField("BASE_SITEMAPS", SITEMAPS_PATH);
    }

    public static void discardTestEnv() throws IOException {
        FileUtils.cleanDirectory(new File(SITES_PATH));
        FileUtils.cleanDirectory(new File(ARCHIVED_PATH));
        FileUtils.cleanDirectory(new File(SITEMAPS_PATH));
    }
}
