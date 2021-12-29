package cn.nbbandxdd.survey.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * <p>实体转换。用于VO实例与Entity实例间转换。
 *
 * <ul>
 * <li>实体转换，使用{@link #map(Object, Class)}。</li>
 * <li>实体集合转换，使用{@link #map(Object, Type)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class ModelMapper implements InitializingBean {

    /**
     * <p>ModelMapper实例。
     */
    private static ModelMapper instance;

    /**
     * <p>构造器。
     */
    public ModelMapper() {

        modelMapper = new org.modelmapper.ModelMapper();
    }

    /**
     * <p>获取实体转换单例。
     *
     * @return 实体转换单例
     */
    public static ModelMapper instance() {

        return instance;
    }

    /**
     * modelMapper
     */
    private final org.modelmapper.ModelMapper modelMapper;

    /**
     * afterPropertiesSet
     */
    @Override
    public void afterPropertiesSet() {

        instance = this;
    }

    /**
     * <p>实体转换。
     *
     * @param src 一般为VO实体实例
     * @param dstType 一般为Entity实体类型
     * @param <D> 一般为Entity实体
     * @return 一般为Entity实体实例
     */
    public static <D> D map(Object src, Class<D> dstType) {

        return instance().modelMapper.map(src, dstType);
    }

    /**
     * <p>实体集合转换。
     *
     * @param src 一般为VO实体实例集合
     * @param dstType 一般为Entity实体集合类型
     * @param <D> 一般为Entity实体
     * @return 一般为Entity实体实例集合
     */
    public static <D> D map(Object src, Type dstType) {

        return instance().modelMapper.map(src, dstType);
    }
}
