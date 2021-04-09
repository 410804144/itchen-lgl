package vip.itchen.support.sequence.dict;

public enum SeqType {
    /**
     * UID生成
     */
    UUID,
    /**
     * REDIS方式生成
     */
    REDIS,
    /**
     * 雪花算法
     */
    SNOWFLAKE,
    /**
     * hashIds算法
     * 参考 https://hashids.org/java/
     */
    HASH_IDS,
    /**
     * 数据库行锁自增长方式
     */
    UNIQUE_KEY,
}