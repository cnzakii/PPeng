package fun.zhub.ppeng.rabbitmqListener;

import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.canal.MyCanalUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static fun.zhub.ppeng.constant.RabbitConstants.*;

/**
 * 监听Canal队列，处理数据库表中变化的数据<br>
 * 例如：插入新的数据，更新数据，删除旧数据
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@Component
@Slf4j
public class CanalListener {


    @Resource
    private ApplicationContext applicationContext;


    /**
     * Canal消息监听队列
     *
     * @param json Canal传输的json字符串
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RECIPE_CANAL_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_CANAL_DATA
    ))
    public void listenCanalQueue(String json) {
        log.info(json);

        // 获取表名
        String table = MyCanalUtil.getTable(json);

        // 查找带有@CanalTable("table")注解的Bean对象
        Object bean = Optional.ofNullable(getCanalTableBean(table))
                .orElseThrow(() -> new RuntimeException("Cannot find the corresponding bean"));


        // 获取操作类型-INSERT UPDATE DELETE
        String type = MyCanalUtil.getType(json);


        // 根据bean和type 获取对应方法
        Method method = Optional.ofNullable(getMethodByTypeName(bean, type))
                .orElseThrow(() -> new RuntimeException("Cannot find the corresponding method"));


        // 获取 bean 对象的父类泛型类型
        Class<?> methodType = Optional.ofNullable(getGenericSuperClassType(bean))
                .orElseThrow(() -> new RuntimeException("Failed to get the generic type"));


        // 获取变化的数据集合
        List<?> changeData = Optional.ofNullable(MyCanalUtil.getChangeData(json, methodType))
                .orElseThrow(() -> new RuntimeException("Change data list is empty"));


        try {
            // 如果是更新操作，就多传入一个参数
            if (Objects.equals(type, "UPDATE")) {
                // 获取旧数据集合
                List<?> oldData = MyCanalUtil.getOldData(json, methodType);
                // 执行更新操作
                method.invoke(bean, oldData, changeData);
                return;
            }

            // 插入和删除 仅传入一个参数
            method.invoke(bean, changeData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取 bean 对象的父类泛型类型
     *
     * @param bean 传入的 bean 对象
     * @return 返回参数类型的 Class 对象，如果获取失败返回 null
     */
    private Class<?> getGenericSuperClassType(Object bean) {
        // 获取 bean 对象的泛型父类 Type 对象
        Type genericSuperclass = bean.getClass().getGenericSuperclass();

        // 判断泛型父类是否为 ParameterizedType 对象
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            // 获取 ParameterizedType 中的实际类型参数
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            // 判断实际类型参数是否存在，并且是否为 Class 对象
            if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?> genericType) {
                return genericType;
            }
        }
        return null;
    }


    /**
     * 根据type获取对应bean对象中的方法
     *
     * @param bean bean对象
     * @param type 类型
     * @return method
     */
    private Method getMethodByTypeName(Object bean, String type) {
        String methodName = switch (type) {
            case "INSERT" -> "insertList";
            case "UPDATE" -> "updateList";
            case "DELETE" -> "deleteList";
            default -> throw new RuntimeException("type error");
        };

        return Arrays.stream(bean.getClass().getMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElse(null);
    }


    /**
     * 查找带有@CanalTable("table")注解的Bean对象
     *
     * @param table 表名
     * @return bean对象
     */
    private Object getCanalTableBean(String table) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(CanalTable.class);

        return beans.values().stream()
                .filter(bean -> AnnotationUtils.findAnnotation(bean.getClass(), CanalTable.class) != null)
                .filter(bean -> Objects.requireNonNull(AnnotationUtils.findAnnotation(bean.getClass(), CanalTable.class)).value().equals(table))
                .findFirst()
                .orElse(null);
    }
}
