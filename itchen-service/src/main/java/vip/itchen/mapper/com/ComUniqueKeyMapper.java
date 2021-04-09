package vip.itchen.mapper.com;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.itchen.domain.com.ComUniqueKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * COM.自增序列号 Mapper 接口
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface ComUniqueKeyMapper extends BaseMapper<ComUniqueKey> {

    /**
     * 获取表数据
     * 同时添加行级排他锁
     * @param uniqueType 类型
     * @return 表数据
     */
    @Select("Select * " +
            "  From com_unique_key " +
            " Where unique_type = #{uniqueType} " +
            " For Update")
    ComUniqueKey lockByType(@Param("seqType") String uniqueType);
}
