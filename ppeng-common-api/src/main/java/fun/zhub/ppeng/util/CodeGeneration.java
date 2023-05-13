//package fun.zhub.ppeng.util;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.generator.FastAutoGenerator;
//import com.baomidou.mybatisplus.generator.config.OutputFile;
//import com.baomidou.mybatisplus.generator.config.rules.DateType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>
// *  Mybatis-plus 代码生成器(新)
// * <p>
// *
// * @author Zaki
// * @version 2.0
// * @since 2023-03-17
// **/
//public class CodeGeneration {
//    /**
//     * 根据表名生成相应结构代码
//     *
//     * @param tableName 表名
//     */
//    public static void generation(String url, String username, String password, String... tableName) {
//        FastAutoGenerator.create(url, username, password)
//                .globalConfig(builder -> {
//                    builder.author("Zaki")
//                            .outputDir(System.getProperty("user.dir") + "/src/main/java")// 指定输出目录
//                            .dateType(DateType.TIME_PACK); // 配置时间策略
//                })
//
//                .packageConfig(builder -> {
//                    builder.entity("entity") // 实体类包名
//                            .parent("fun.zhub.ppeng")// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
//                            .controller("controller")// 控制层包名
//                            .mapper("mapper")// mapper层包名
//                            .service("service")// service层包名
//                            .serviceImpl("service.impl")// service实现类包名
//                            //自定义mapper.xml文件输出目录
//                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper"));
//                })
//                .strategyConfig(builder -> {
//                    //设置要生成的表名
//                    builder.addInclude(tableName)
//                            .addTablePrefix("t_")
//                            .entityBuilder()
//                            .enableLombok()
//                            .enableChainModel()
//                            .naming(NamingStrategy.underline_to_camel)// 数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
//                            .columnNaming(NamingStrategy.underline_to_camel)// 表字段映射实体属性命名规则：默认null，不指定按照naming执行
//                            .idType(IdType.AUTO)// 添加全局主键类型
//                            .formatFileName("%s")// 格式化实体名称，%s取消首字母I,
//                            .mapperBuilder()
//                            .enableMapperAnnotation()// 开启mapper注解
//                            .enableBaseResultMap()// 启用xml文件中的BaseResultMap 生成
//                            .enableBaseColumnList()// 启用xml文件中的BaseColumnList
//                            .formatMapperFileName("%sMapper")// 格式化Dao类名称
//                            .formatXmlFileName("%sMapper")// 格式化xml文件名称
//                            .serviceBuilder()
//                            .formatServiceFileName("%sService")// 格式化 service 接口文件名称
//                            .formatServiceImplFileName("%sServiceImpl")// 格式化 service 接口文件名称
//                            .controllerBuilder()
//                            .enableRestStyle();
//                }).injectionConfig(consumer -> {
//                    Map<String, String> customFile = new HashMap<>();
//                    consumer.customFile(customFile);
//                })
//                .execute();
//    }
//}
