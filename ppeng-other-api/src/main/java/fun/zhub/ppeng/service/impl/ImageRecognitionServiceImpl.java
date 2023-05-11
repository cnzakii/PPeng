package fun.zhub.ppeng.service.impl;

import com.baidu.aip.imageclassify.AipImageClassify;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.ImageRecognitionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 图片识别实现类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-11
 **/
@Service
@Slf4j
public class ImageRecognitionServiceImpl implements ImageRecognitionService {


    @Resource
    private AipImageClassify client;


    /**
     * 菜品识别
     *
     * @param dish 菜品文件
     * @return key为菜品名称  value为可信度
     */
    @Override
    public Map<String, Double> recognizeDish(MultipartFile dish) {

        // 设置查询参数
        HashMap<String, String> options = new HashMap<>();
        // 返回预测得分top结果数，默认为5
        options.put("top_num", "3");
        // 默认0.95，可以通过该参数调节识别效果，降低非菜识别率
        options.put("filter_threshold", "0.7");


        byte[] bytes;
        try {
            bytes = dish.getBytes();
        } catch (IOException e) {
            log.error("识别菜品失败====》读取字节数据异常:{}", e.getLocalizedMessage());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "识别菜品失败");
        }

        JSONObject object = client.dishDetect(bytes, options);

        log.info(object.toString());

        // 获取结果
        JSONArray jsonArray;
        try {
            jsonArray = object.getJSONArray("result");
        } catch (Exception e) {
            log.error("识别菜品失败====》调用API失败:{}", object);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "识别菜品失败");
        }

        // 提取菜品名和可信度
        Map<String, Double> map;
        map = IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .collect(Collectors.toMap(
                        jsonObject -> jsonObject.getString("name"),
                        jsonObject -> Double.parseDouble(jsonObject.getString("probability"))
                ));

        return map;
    }
}
