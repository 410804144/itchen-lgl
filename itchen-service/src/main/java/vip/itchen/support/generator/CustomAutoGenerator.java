package vip.itchen.support.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by lhb on 2019/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomAutoGenerator extends AutoGenerator {

    private String customXmlPath;

    /**
     * 生成开关
     * true:生成
     * false:不生成
     */
    private Boolean serviceSwitch = true;
    private Boolean entitySwitch = true;
    private Boolean mapperSwitch = true;

    private String bizModuleName = "";

    @Override
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        return customerHandlerPackage(super.pretreatmentConfigBuilder(config));
    }

    private ConfigBuilder customerHandlerPackage(ConfigBuilder builder) {
        TemplateConfig template = builder.getTemplate();
        String outputDir = builder.getGlobalConfig().getOutputDir();
        PackageConfig config = null == getPackageInfo() ? new PackageConfig() : getPackageInfo();
        Map<String, String> packageInfo = builder.getPackageInfo();

        if (StringUtils.isNotBlank(bizModuleName)) {
            packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
            packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
            packageInfo.put(ConstVal.XML, createXmlPackage(config.getParent()));
            packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
            packageInfo.put(ConstVal.SERVICE_IMPL, createServiceImplPackage(config.getParent()));
            packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));

            setPathInfo(builder.getPathInfo(), template.getEntity(getGlobalConfig().isKotlin()), outputDir, ConstVal.ENTITY_PATH, packageInfo.get(ConstVal.ENTITY));
            setPathInfo(builder.getPathInfo(), template.getMapper(), outputDir, ConstVal.MAPPER_PATH, packageInfo.get(ConstVal.MAPPER));
            setPathInfo(builder.getPathInfo(), template.getXml(), outputDir, ConstVal.XML_PATH, packageInfo.get(ConstVal.XML));
            setPathInfo(builder.getPathInfo(), template.getService(), outputDir, ConstVal.SERVICE_PATH, packageInfo.get(ConstVal.SERVICE));
            setPathInfo(builder.getPathInfo(), template.getServiceImpl(), outputDir, ConstVal.SERVICE_IMPL_PATH, packageInfo.get(ConstVal.SERVICE_IMPL));
            setPathInfo(builder.getPathInfo(), template.getController(), outputDir, ConstVal.CONTROLLER_PATH, packageInfo.get(ConstVal.CONTROLLER));
        }
        // Controller不需要生成
        builder.getPathInfo().remove(ConstVal.CONTROLLER_PATH);
        if (!serviceSwitch) {
            builder.getPathInfo().remove(ConstVal.SERVICE_PATH);
            builder.getPathInfo().remove(ConstVal.SERVICE_IMPL_PATH);
        }
        if (!entitySwitch) {
            builder.getPathInfo().remove(ConstVal.ENTITY_PATH);
        }
        if (!mapperSwitch) {
            builder.getPathInfo().remove(ConstVal.MAPPER_PATH);
        }

        if (StringUtils.isNotBlank(customXmlPath)) {
            String basePath = StringUtils.removeEnd(customXmlPath, "/");
            if (StringUtils.isNotBlank(bizModuleName)) {
                basePath = basePath.concat("/").concat(bizModuleName);
            }
            builder.getPathInfo().put(ConstVal.XML_PATH, basePath);
        }
        return builder;
    }

    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isBlank(parent)) {
            return subPackage + StringPool.DOT + bizModuleName;
        }
        return parent + StringPool.DOT + subPackage + StringPool.DOT + bizModuleName;
    }

    private String createXmlPackage(String parent) {
        if (StringUtils.isBlank(parent)) {
            return bizModuleName + StringPool.DOT + "mapper.xml";
        }
        return parent + StringPool.DOT + bizModuleName + StringPool.DOT + "mapper.xml";
    }

    private String createServiceImplPackage(String parent) {
        if (StringUtils.isBlank(parent)) {
            return "service." + bizModuleName + ".impl";
        }
        return parent + ".service." + bizModuleName + ".impl";
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String pathKey, String packageName) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(pathKey, joinPath(outputDir, packageName));
        }
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!com.baomidou.mybatisplus.core.toolkit.StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }
}
