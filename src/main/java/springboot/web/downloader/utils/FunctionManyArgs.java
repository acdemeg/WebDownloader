package springboot.web.downloader.utils;

@FunctionalInterface
public interface FunctionManyArgs<T, S, R> {

    /**
     * Applies this function to the given argument.
     * @param s1 taskId
     * @param s2 URI
     * @param t TypeTask
     * @return the function result
     */
    R apply(S s1, S s2, T t);
}