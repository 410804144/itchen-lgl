package vip.itchen.support.i18n;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import vip.itchen.support.i18n.vo.I18nSource;
import vip.itchen.support.i18n.vo.I18nVo;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateBatchRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateBatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 国际化翻译（https://cloud.tencent.com/document/product/551/15619）
 * @author chenkh
 * @date 2021-01-06
 */
@Slf4j
public class I18nTranslate {

    /**
     * 翻译的项目名称
     */
    private static final String PROJECT = "itchen-service";
    /**
     * 国际化前缀
     */
    private static final String FILE_PREFIX;
    /**
     * 国际化后缀
     */
    private static final String FILE_SUFFIX = ".properties";
    /**
     * 翻译secretId
     */
    private static final String SECRET_ID = "AKIDNvhT2MD0kFYdVOpEXXLwINc6KlaAlMLs";
    /**
     * 翻译secretKey
     */
    private static final String SECRET_KEY = "UseFcKvfAWorvqJZaKH1ay687qITb2Ow";
    /**
     * 翻译region
     */
    private static final String REGION = "ap-guangzhou";

    /**
     * 原始语言
     */
    private static final I18nSource FROM_SOURCE = I18nSource.zh_CN;

    /**
     * 要翻译的语言
     */
    private static final List<I18nSource> TO_SOURCE_LIST = Arrays.asList(
            I18nSource.zh_TW,
            I18nSource.en_US
    );

    static {
        FILE_PREFIX = new File("").getAbsolutePath()
                .concat("/")
                .concat(PROJECT)
                .concat("/src/main/resources/base/i18n/messages");
    }

    public static void main(String[] args) throws IOException {
        log.info("开始翻译");

        List<I18nVo> fromDataList = readData(FILE_PREFIX.concat(FROM_SOURCE.getFilename()).concat(FILE_SUFFIX));
        for (I18nSource toSource : TO_SOURCE_LIST) {
            translate(fromDataList, toSource);
        }

        log.info("所有语言翻译完成");
    }

    /**
     * 翻译
     * @param fromDataList 原始数据
     * @param toSource 目标语言
     */
    private static void translate(List<I18nVo> fromDataList, I18nSource toSource) throws IOException {
        log.warn("============={} => {} 开始=============", FROM_SOURCE.name(), toSource.name());
        String toFilename = FILE_PREFIX.concat(toSource.getFilename()).concat(FILE_SUFFIX);
        List<I18nVo> toDataList = readData(toFilename);

        toDataList = merge(fromDataList, toDataList);

        List<I18nVo> translateList = toDataList
                .stream()
                .filter(item -> StrUtil.isNotBlank(item.getOldValue()))
                .collect(Collectors.toList());
        log.info("需要翻译条数：{}", translateList.size());
        batchTmt(translateList, toSource);
        log.info("写入数据到文件中");
        writeData(toFilename, toDataList);
        log.info("============={} => {} 完成=============", I18nTranslate.FROM_SOURCE.name(), toSource.name());
    }

    /**
     * 批量调用腾讯翻译
     * @param list 列表
     */
    private static void batchTmt(List<I18nVo> list, I18nSource toSource) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 请求有频率限制（5次/秒），所以加个睡眠
        ThreadUtil.sleep(1000);
        List<String> textList = list.stream().map(I18nVo::getOldValue).collect(Collectors.toList());
        try {
            Credential credential = new Credential(SECRET_ID, SECRET_KEY);

            TmtClient client = new TmtClient(credential, REGION);

            TextTranslateBatchRequest req = new TextTranslateBatchRequest();
            req.setProjectId(0L);
            req.setSource(FROM_SOURCE.getSource());
            req.setSourceTextList(textList.toArray(new String[0]));
            req.setTarget(toSource.getSource());
            TextTranslateBatchResponse resp = client.TextTranslateBatch(req);
            if (resp.getTargetTextList().length != textList.size()) {
                log.error("翻译失败，传入{}条数据，解析出{}条数据", textList.size(), resp.getTargetTextList().length);
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setValue(resp.getTargetTextList()[i]);
            }
        } catch (TencentCloudSDKException e) {
            log.error("翻译失败：" + e.getMessage(), e);
        }
    }

    /**
     * 两种语言合并
     * @param fromDataList 原始语言
     * @param toDataList 目标语言
     * @return 合并后的语言
     */
    private static List<I18nVo> merge(List<I18nVo> fromDataList, List<I18nVo> toDataList) {
        List<I18nVo> mergeList = new ArrayList<>();
        for (I18nVo vo : fromDataList) {
            if (!vo.isNeedTranslate()) {
                mergeList.add(vo);
            } else {
                I18nVo tmp = toDataList.stream().filter(item -> Objects.equals(vo.getKey(), item.getKey())).findAny().orElse(null);
                if (null == tmp || StrUtil.isBlank(tmp.getValue())) {
                    mergeList.add(new I18nVo(vo.isNeedTranslate(), vo.getKey(), vo.getValue(), ""));
                } else {
                    mergeList.add(tmp);
                }
            }
        }
        return mergeList;
    }

    /**
     * 写数据
     * @param filename 文件名
     * @param list 数据
     */
    private static void writeData(String filename, List<I18nVo> list) throws IOException {
        File file = ResourceUtils.getFile(filename);
        OutputStream os = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("");
        for (I18nVo vo : list) {
            bw.append(vo.toString()).append("\n");
        }
        bw.close();
        writer.close();
        os.close();
    }

    /**
     * 获取资源数据
     * @param filename 文件名
     * @return 文件文本内容
     */
    private static List<I18nVo> readData(String filename) throws IOException {
        File file = ResourceUtils.getFile(filename);
        InputStream is = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        List<I18nVo> list = new ArrayList<>();
        while(true) {
            String line = br.readLine();
            if (null == line) {
                break;
            }
            String str = UnicodeUtil.toString(line).trim();
            if (str.startsWith("#") || !str.contains("=")) {
                list.add(new I18nVo(false, "", "", str));
            } else {
                int index = str.indexOf("=");
                list.add(new I18nVo(true, str.substring(0, index).trim(), "", str.substring(index + 1).trim()));
            }
        }
        br.close();
        reader.close();
        is.close();
        return list;
    }
}


