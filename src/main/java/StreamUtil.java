import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class StreamUtil {

    public static <T, R> Map<R, List<T>> groupingBy(List<T> dataList, Function<T, R> groupingKey){
        return dataList
                .stream()
                .filter(v -> v!=null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey)
                );
    }

    public static <T, K, V> Map<K, V> listToMap(List<T> dataList, Function<T, K> keyFunc, Function<T, V> valueFunc){
        return dataList
                .stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.toMap(keyFunc, valueFunc, (v1, v2) -> v1)
                );
    }

    public static <T> List<T> distinctList(List<T> dataList){
        return distinctList(dataList, Function.identity());
    }

    public static <T, R> List<R> distinctList(List<T> dataList, Function<T, R> mappingKey){
        return dataList
                .stream()
                .filter(Objects::nonNull)
                .map(mappingKey)
                .distinct()
                .collect(Collectors.toList());
    }

    public static <T, R> Map<R, Long> groupingCount(List<T> dataList, Function<T, R> groupingKey){
        return dataList
                .stream()
                .filter(v -> v!=null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey, Collectors.counting())
                );
    }


    public static <T, R> Map<R, Integer> groupingSum(List<T> dataList, Function<T, R> groupingKey, ToIntFunction<T> summingKey){
        return dataList
                .stream()
                .filter(v -> v != null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey, Collectors.summingInt(summingKey))
                );
    }

    public static <T, R> Map<R, Map<R, Long>> groupingThenGroupingCount(List<T> dataList,
                                                                Function<T, R> groupingKey1,
                                                                Function<T, R> groupingKey2){
        return dataList
                .stream()
                .filter(v -> v!=null && groupingKey1.apply(v) != null && groupingKey2.apply(v) != null)
                .collect(
                        Collectors.groupingBy(
                                groupingKey1, Collectors.groupingBy(groupingKey2, Collectors.counting()
                                )
                        )
                );
    }

    public static <T, R> Map<R, Integer> groupingThenDistinctCount(List<T> dataList,
                                                                Function<T, R> groupingKey,
                                                                Function<T, R> distinctKey){
        return dataList
                .stream()
                .filter(v -> v!=null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey,
                                Collectors.mapping(distinctKey,
                                        Collectors.collectingAndThen(Collectors.toSet(), Set::size)
                                )
                        )
                );
    }

    public static <T, E, R> Map<E, Set<R>> groupingThenDistinctSet(List<T> dataList,
                                                                     Function<T, E> groupingKey,
                                                                     Function<T, R> distinctKey){
        return dataList
                .stream()
                .filter(v -> v != null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey,
                                Collectors.mapping(distinctKey, Collectors.toSet())
                        )
                );
    }


    public static <T, E, R> Map<E, List<R>> groupingThenDistinctList(List<T> dataList,
                                                                   Function<T, E> groupingKey,
                                                                   Function<T, R> distinctKey){
        return dataList
                .stream()
                .filter(v -> v != null && groupingKey.apply(v) != null)
                .collect(
                        Collectors.groupingBy(groupingKey,
                                Collectors.mapping(distinctKey,
                                        Collectors.collectingAndThen(Collectors.toList(), StreamUtil::distinctList)
                                )
                        )
                );
    }



}
