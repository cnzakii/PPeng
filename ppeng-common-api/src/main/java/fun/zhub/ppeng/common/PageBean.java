package fun.zhub.ppeng.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果返回对象<br>
 * 除了分页查询结果集合，还包括了最大/最小的时间戳
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-16
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageBean<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -4101605061864584090L;

    /**
     * 分页查询结果
     */
    private List<T> resultList;

    /**
     * 时间戳
     */
    private Long timestamp;
}
