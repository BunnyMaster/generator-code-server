package com.auth.module.generator.service;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.DatabaseConfigEntity;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.SupportEnumsVO;

import java.util.List;

/**
 * 配置服务
 *
 * @author bunny
 */
public interface ConfigService {
    /**
     * 获取数据库配置
     *
     * @return 数据库配置
     */
    GenerationConfigDTO<DatabaseConfigEntity> getDatabaseConfig();

    /**
     * 获取 FilenameTypeEnums
     *
     * @return FilenameTypeEnums
     */
    List<ElementOptionVO> getFilenameTypeEnums();

    /**
     * 支持哪些生成内容
     *
     * @return 支持哪些生成内容
     */
    SupportEnumsVO getSupportEnums();

}
