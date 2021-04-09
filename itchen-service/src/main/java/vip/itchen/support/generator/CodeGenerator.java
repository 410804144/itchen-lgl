package vip.itchen.support.generator;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 代码生成器
 * @author ckh
 * @date 2021-04-09
 */
public class CodeGenerator {

    protected static void doGenerator(JSONObject configObj) {
        CustomAutoGenerator mpg = new CustomAutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir").concat("/").concat(configObj.getString(GeneratorConstVal.FILE_STORE_MODULE));
        gc.setOutputDir(projectPath.concat("/src/main/java"));
        gc.setAuthor(configObj.getString(GeneratorConstVal.AUTHOR_NAME));
        gc.setFileOverride(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setOpen(false);
        gc.setIdType(IdType.AUTO);
        gc.setDateType(DateType.ONLY_DATE);

        if (StringUtils.isNotBlank(configObj.getString(GeneratorConstVal.ENTITY_NAME))) {
            gc.setEntityName(configObj.getString(GeneratorConstVal.ENTITY_NAME));
        }
        if (StringUtils.isNotBlank(configObj.getString(GeneratorConstVal.MAPPER_NAME))) {
            gc.setMapperName(configObj.getString(GeneratorConstVal.MAPPER_NAME));
        }
        if (StringUtils.isNotBlank(configObj.getString(GeneratorConstVal.XML_NAME))) {
            gc.setXmlName(configObj.getString(GeneratorConstVal.XML_NAME));
        }
        if (StringUtils.isNotBlank(configObj.getString(GeneratorConstVal.SERVICE_NAME))) {
            gc.setServiceName(configObj.getString(GeneratorConstVal.SERVICE_NAME));
        }
        if (StringUtils.isNotBlank(configObj.getString(GeneratorConstVal.SERVICE_IMPL_NAME))) {
            gc.setServiceImplName(configObj.getString(GeneratorConstVal.SERVICE_IMPL_NAME));
        }
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(configObj.getString(GeneratorConstVal.DB_URL));
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(configObj.getString(GeneratorConstVal.DB_USERNAME));
        dsc.setPassword(configObj.getString(GeneratorConstVal.DB_PASSWORD));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("vip.itchen");
        pc.setModuleName("");
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);

        // 是否生成service文件
        if (null != configObj.getBoolean(GeneratorConstVal.SERVICE_SWITCH)) {
            mpg.setServiceSwitch(configObj.getBoolean(GeneratorConstVal.SERVICE_SWITCH));
        }
        if (null != configObj.getBoolean(GeneratorConstVal.ENTITY_SWITCH)) {
            mpg.setEntitySwitch(configObj.getBoolean(GeneratorConstVal.ENTITY_SWITCH));
        }
        if (null != configObj.getBoolean(GeneratorConstVal.MAPPER_SWITCH)) {
            mpg.setMapperSwitch(configObj.getBoolean(GeneratorConstVal.MAPPER_SWITCH));
        }
        // XML文件夹路径
        mpg.setCustomXmlPath(projectPath.concat("/src/main/resources/base/mapper"));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("");
        strategy.setInclude(configObj.getJSONArray(GeneratorConstVal.TABLE_LIST).stream().map(Object::toString).toArray(String[]::new));
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);

        // 业务模块名
        mpg.setBizModuleName(configObj.getString(GeneratorConstVal.BIZ_MODULE_NAME));

        mpg.execute();
    }

    public static void main(String[] args) {

        JSONObject configObj = new JSONObject();
        // 作者
        configObj.put(GeneratorConstVal.AUTHOR_NAME, "ckh");
        // 数据库URL
        configObj.put(GeneratorConstVal.DB_URL, "jdbc:mysql://localhost:3306/itchen-lgl?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&failOverReadOnly=false&allowMultiQueries=true&useSSL=false");
        // 数据库用户名
        configObj.put(GeneratorConstVal.DB_USERNAME, "root");
        // 数据库密码
        configObj.put(GeneratorConstVal.DB_PASSWORD, "root1234");
        // 是否生成service文件 true:生成 false:不生成
        configObj.put(GeneratorConstVal.SERVICE_SWITCH, true);
        // 是否生成entity文件 true:生成 false:不生成
        configObj.put(GeneratorConstVal.ENTITY_SWITCH, true);
        // 是否生成mapper.java文件 true:生成 false:不生成
        configObj.put(GeneratorConstVal.MAPPER_SWITCH, true);
        // 文件存储的pom模块名
        configObj.put(GeneratorConstVal.FILE_STORE_MODULE, "itchen-service");
        // 业务模块名（指定该模块名后，各层文件的包名均会增加一层模块名）
        // 例: 表[bfc_user] 模块名为空 => 生成的entity类为 com.XXX.domain.BfcUser
        // 例: 表[bfc_user] 模块名[user] => 生成的entity类为 com.XXX.domain.user.BfcUser
        configObj.put(GeneratorConstVal.BIZ_MODULE_NAME, "po");
        // 生成对象的表名（可多个，如业务模块不一样，必须分开生成）
        configObj.put(GeneratorConstVal.TABLE_LIST, Arrays.asList(
                "po_item"
        ));

        // 执行生成操作
        doGenerator(configObj);
    }
}
