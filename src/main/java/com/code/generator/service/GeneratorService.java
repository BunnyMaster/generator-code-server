package com.code.generator.service;

import com.code.generator.model.dto.VmsArgumentDto;
import com.code.generator.model.vo.GeneratorVo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 代码生成服务接口
 * 提供基于数据库和SQL的代码生成功能
 */
public interface GeneratorService {

    /**
     * 根据数据库表生成代码
     *
     * @param dto 生成参数
     * @return 生成的代码，按表名分组
     */
    Map<String, List<GeneratorVo>> generateCodeByDatabase(VmsArgumentDto dto);

    /**
     * 根据SQL语句生成代码
     *
     * @param dto 生成参数
     * @return 生成的代码，按表名分组
     */
    Map<String, List<GeneratorVo>> generateCodeBySql(VmsArgumentDto dto);

    /**
     * 打包数据库生成的代码为ZIP下载
     *
     * @param dto 生成参数
     * @return ZIP文件响应实体
     */
    ResponseEntity<byte[]> downloadByZipByDatabase(@Valid VmsArgumentDto dto);

    /**
     * 打包SQL生成的代码为ZIP下载
     *
     * @param dto 生成参数
     * @return ZIP文件响应实体
     */
    ResponseEntity<byte[]> downloadByZipBySqL(@Valid VmsArgumentDto dto);

}
