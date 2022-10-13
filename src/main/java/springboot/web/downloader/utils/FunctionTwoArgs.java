package springboot.web.downloader.utils;

@FunctionalInterface
public interface FunctionTwoArgs<T, S, R> {

    /**
     * Applies this function to the given argument.
     */
    R apply(S s, T t);
}