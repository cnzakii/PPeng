package fun.zhub.ppeng.canal;

import java.util.List;
import java.util.stream.IntStream;

/**
 * 接收Canal数据，进行处理的抽象类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-07
 **/
public abstract class AbstractCanalHandler<T> {
    /**
     * 发生插入操作的处理方法--单条
     *
     * @param data 插入的数据
     */
    public abstract void insert(T data);


    /**
     * 发生更新操作的处理方法--单条
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    public abstract void update(T oldData, T newData);

    /**
     * 发生删除操作的处理方法--单条
     *
     * @param data 删除操作
     */
    public abstract void delete(T data);

    /**
     * 发生插入操作的处理方法--多条
     *
     * @param dataList 插入的数据
     */
    public void insertList(List<T> dataList) {
        dataList.forEach(this::insert);
    }


    /**
     * 发生更新操作的处理方法--多条
     *
     * @param oldList 旧数据
     * @param newList 新数据
     */
    public void updateList(List<T> oldList, List<T> newList) {
        IntStream.range(0, newList.size())
                .forEach(i -> update(oldList.get(i), newList.get(i)));
    }

    /**
     * 发生删除操作的处理方法--多条
     *
     * @param dataList 删除操作
     */
    public void deleteList(List<T> dataList) {
        dataList.forEach(this::delete);
    }
}
