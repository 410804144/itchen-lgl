package vip.itchen.dict.ac;

/**
 * 黑名单类型
 * @author ckh
 * @date 2021-04-09
 */
public enum BlackType {
    LOGIN("登录"),
    ACTIVITY("动态"),
    ;

    private final String desc;

    BlackType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
