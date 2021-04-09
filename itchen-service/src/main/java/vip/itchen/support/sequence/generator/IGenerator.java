package vip.itchen.support.sequence.generator;

public interface IGenerator {
    /**
     * 生成序列号
     * @return 序列号
     */
    Long nextId();

    /**
     * 生成序列号
     * @return 序列号
     */
    String nextIdString();
}
