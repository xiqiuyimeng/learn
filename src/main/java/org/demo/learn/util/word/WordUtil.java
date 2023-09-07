package org.demo.learn.util.word;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * word动态表格的处理办法：先将word文档，另存为xml文件，在xml文件中填写freemarker语法，然后重命名为 ftl格式模板文件，使用freemarker渲染即可
 * @author luwt-a
 * @date 2023/8/31
 */
@Slf4j
@Data
public class WordUtil {

    private static final Configuration configuration;

    private Template template;


    static {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(UTF_8.displayName());
        configuration.setClassicCompatible(true);
    }


    public WordUtil(String ftlFile) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(ftlFile);
            InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            template = new Template(ftlFile, reader, configuration);
        } catch (IOException e) {
            log.error("加载 WordUtil 模板资源失败", e);
        }
    }

    public static void createParentDir(File parentDir) {
        // 创建父级目录
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                log.info("父级目录 {} 已成功创建！", parentDir);
            } else {
                log.error("父级目录 {} 创建失败", parentDir);
            }
        }
    }

    //生成word文档方法
    public <T> File createDoc(T data, String filename) {
        File file = new File(filename);
        // 创建父级目录
        WordUtil.createParentDir(file.getParentFile());

        Writer writer = null;
        FileOutputStream fos = null;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, UTF_8);
            //不要偷懒写成下面酱紫: 否则无法关闭fos流，打zip包时存取被拒抛异常
            //w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            template.process(data, writer);
        } catch (Exception e) {
            log.error("WordUtil渲染doc文档失败", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                log.error("WordUtil关闭流失败", e);
            }
        }
        return file;
    }

    public static void main(String[] args) throws Exception {
        // 调用word文档帮助类
        WordUtil wordUtil = new WordUtil("/freemarker_templates/测试模板.ftl");
        // 初始化数据，用hashmap也可以
        ValueMap valueMap = new ValueMap();

        // 动态表格数据
        List<Value> values = new ArrayList<>();
        values.add(new Value("内容1-1", "内容2-1", "内容3-1", "内容4-1", "内容5-1", "内容6-1"));
        values.add(new Value("内容1-2", "内容2-2", "内容3-2", "内容4-2", "内容5-2", "内容6-2"));
        values.add(new Value("内容1-3", "内容2-3", "内容3-3", "内容4-3", "内容5-3", "内容6-3"));
        values.add(new Value("内容1-4", "内容2-4", "内容3-4", "内容4-4", "内容5-4", "内容6-4"));

        valueMap.setValues(values);
        valueMap.setSex("男");
        wordUtil.createDoc(valueMap, "测试.docx");
    }

    @Data
    public static class ValueMap {
        private List<Value> values;
        private String sex;
        private String birthday;
        private String nation;
        private String country;
        private String domicile;
    }

    @Data
    @AllArgsConstructor
    public static class Value {
        private String value1;
        private String value2;
        private String value3;
        private String value4;
        private String value5;
        private String value6;
    }

}
