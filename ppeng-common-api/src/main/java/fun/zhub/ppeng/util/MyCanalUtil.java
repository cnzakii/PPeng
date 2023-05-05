package fun.zhub.ppeng.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 处理Canal通过MQ发送过来的数据
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-05
 **/
public class MyCanalUtil {


    /**
     * 获取新数据集合
     *
     * @param json  MQ监听到的json字符串
     * @param clazz 类
     * @param <T>   泛型
     * @return list<T>
     */
    public static <T> List<T> getChangeData(String json, Class<T> clazz) {
        return getDataList(json, "data", clazz);
    }


    /**
     * 获取旧数据集合
     *
     * @param json  MQ监听到的json字符串
     * @param clazz 类
     * @param <T>   泛型
     * @return 旧数据
     */
    public static <T> List<T> getOldData(String json, Class<T> clazz) {

        List<T> newData = getChangeData(json, clazz);
        List<T> oldData = getDataList(json, "old", clazz);

        if (CollUtil.isEmpty(newData) || CollUtil.isEmpty(oldData)) {
            throw new RuntimeException("List is emtpy");
        }
        if (newData.size() != oldData.size()) {
            throw new RuntimeException("List length is not equal");
        }

        IntStream.range(0, newData.size())
                .forEach(i -> MyBeanUtil.copyPropertiesIgnoreNull(oldData.get(i), newData.get(i)));


        return newData;
    }


    /**
     * 获取表名
     *
     * @param json MQ监听到的json字符串
     * @return 表名
     */
    public static String getTable(String json) {
        return getSingleData(json, "table", String.class);
    }


    /**
     * 获取操作类型-INSERT UPDATE DELETE
     *
     * @param json MQ监听到的json字符串
     * @return INSERT UPDATE DELETE
     */
    public static String getType(String json) {
        return getSingleData(json, "type", String.class);
    }


    /**
     * 获取Object中的数据，并转换成T
     *
     * @param json  MQ监听到的json字符串
     * @param key   json字符串中的key
     * @param clazz 类
     * @param <T>   泛型
     * @return data
     */
    public static <T> T getSingleData(String json, String key, Class<T> clazz) {
        Object o = JSONUtil.parseObj(json).get(key);
        if (Objects.isNull(o)) {
            return null;
        }

        // 检查clazz是否为Bean类型
        if (BeanUtil.isBean(clazz)) {
            return BeanUtil.toBean(o, clazz);
        }

        return clazz.cast(o);
    }

    /**
     * 获取JSONArray中的所有数据，并转换成T
     *
     * @param json  MQ监听到的json字符串
     * @param key   json字符串中的key
     * @param clazz 类
     * @param <T>   泛型
     * @return list<T>
     */
    public static <T> List<T> getDataList(String json, String key, Class<T> clazz) {
        JSONArray data = JSONUtil.parseObj(json).getJSONArray(key);
        if (data.isEmpty()) {
            return null;
        }

        // 检查clazz是否为Bean类型
        if (BeanUtil.isBean(clazz)) {
            return data.stream()
                    .map(one -> BeanUtil.toBean(one, clazz))
                    .toList();
        }

        return data.stream()
                .map(clazz::cast)
                .toList();

    }
}
