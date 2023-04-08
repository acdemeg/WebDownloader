package springboot.web.downloader.utils;

@FunctionalInterface
public interface FunctionManyArgs<T, S, R> {

    /**
     * Applies this function to the given argument.
     */
    R apply(S s1, S s2, T t);
}