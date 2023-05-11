package fun.zhub.ppeng.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 图片识别 interface
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-11
 **/
public interface ImageRecognitionService {

    /**
     * 菜品识别
     *
     * @param dish 菜品文件
     * @return map-> key为菜品名称  value为可信度
     */
    Map<String, Double> recognizeDish(MultipartFile dish) ;
}
