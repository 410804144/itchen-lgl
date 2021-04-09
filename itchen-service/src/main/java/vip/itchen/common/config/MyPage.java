package vip.itchen.common.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author ckh
 * @date 2021-04-09
 */
public class MyPage<T> extends Page<T> {

    public MyPage(Integer current, Integer size) {
        super();
        if (null == current) {
            this.setCurrent(1);
        } else {
            this.setCurrent(current);
        }

        if (null == size) {
            this.setSize(20);
        } else {
            this.setSize(size);
        }

        // 默认不进行Select count查询
        this.setSearchCount(false);
    }
}
