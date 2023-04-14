package fun.zhub.ppeng.service;

import fun.zhub.ppeng.PPengOtherApiApplication;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 *  TODO
 * <p>
 *
 * @author Zaki
 * @version TODO
 * @since 2023-04-14
 **/

@SpringBootTest(classes = PPengOtherApiApplication.class)
@Slf4j
public class TestContentCensor {

    @Resource
    private ContentCensorService contentCensorService;


    @Test
    void TestTrueText() {
        contentCensorService.censorNickName(1L, "PPeng");
        // 合规
        /*
         * {"conclusion":"合规","log_id":16814833457521994,"isHitMd5":false,"conclusionType":1}
         */
    }


    @Test
    void TestFalseText() {
        contentCensorService.censorNickName(1637127723212214272L, "学Java的都是大傻逼");
        // 违规
        /*
         * {"conclusion":"不合规",
         * "log_id":16814834961651329,
         * "data":[
         *          {"msg":"存在低俗辱骂不合规",
         *          "conclusion":"不合规",
         *          "hits":[
         *                   {"wordHitPositions":[
         *                                          {"positions":[[9,10]],"label":"500200","keyword":"傻逼"}
         *                                       ],
         *                     "probability":1,
         *                      "datasetName":"百度默认文本反作弊库",
         *                      "words":["傻逼"],"modelHitPositions":[[0,10,1]]}
         *                  ],
         *          "subType":5,
         *          "conclusionType":2,
         *          "type":12}
         *          ],
         * "isHitMd5":false,"conclusionType":2}
         */
    }

}
