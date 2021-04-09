package vip.itchen.common.handler;

import vip.itchen.common.util.I18nUtil;
import lombok.Data;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
public class RespModel {

    private Boolean success;

    private String description;

    private Object data;

    private String code;

    public static RespModel error(String code) {
        return error(code, I18nUtil.getMessage(code));
    }

    public static RespModel error(String code, Object... args) {
        return error(code, I18nUtil.getMessage(code, args));
    }

    public static RespModel error(String code, String data) {
        RespModel model = new RespModel();
        model.setCode(code);
        model.setSuccess(false);
        model.setDescription(I18nUtil.getMessage("common.00001"));
        model.setData(data);
        return model;
    }

    public static RespModel success(Object data) {
        RespModel model = new RespModel();
        model.setCode("");
        model.setSuccess(true);
        model.setDescription(I18nUtil.getMessage("common.00002"));
        model.setData(data);
        return model;
    }
}
