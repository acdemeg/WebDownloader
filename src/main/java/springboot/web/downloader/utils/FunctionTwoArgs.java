package springboot.web.downloader.utils;

@FunctionalInterface
public interface FunctionTwoArgs<T, S, R> {

    /**
     * Applies this function to the given argument.
     * @param t firs function argument
     * @param s second function argument
     * @return the function result
     */
    R apply(T t, S s);
}