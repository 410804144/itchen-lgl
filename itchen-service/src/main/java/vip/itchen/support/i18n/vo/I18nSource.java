package vip.itchen.support.i18n.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 国际化语言
 * @author chenkh
 * @date 2021-01-07
 */
@Getter
@AllArgsConstructor
public enum I18nSource {

    zh_CN("_zh_CN", "zh", "简体中文"),
    zh_TW("_zh_TW", "zh-TW", "繁体中文"),
    en_US("", "en", "英文"),
    ja("_ja", "ja", "日语"),
    ko("_ko_KR", "ko", "韩语"),
    fr("_fr", "fr", "法语"),
    es("_es", "es", "西班牙语"),
    it("_it", "it", "意大利语言"),
    de("_de", "de", "德语"),
    tr("_tr", "tr", "土耳其语"),
    ru("_ru", "ru", "俄语"),
    pt("_pt", "pt", "葡萄牙语"),
    vi("_vi", "vi", "越南语"),
    id("_id", "id", "印尼语言"),
    th("_th", "th", "泰语"),
    ms("_ms", "ms", "马来西亚语"),
    ;

    private final String filename;
    private final String source;
    private final String desc;

}