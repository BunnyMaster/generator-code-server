package com.code.generator.service.impl;

import com.code.generator.model.vo.VmsPathVo;
import com.code.generator.service.VmsService;
import com.code.generator.utils.ResourceFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * VMS服务主实现类，负责协调各子服务完成代码生成、资源管理和打包下载功能
 */
@Service
@RequiredArgsConstructor
public class VmsServiceImpl implements VmsService {

    /**
     * 获取VMS资源文件路径列表并按类型分组
     *
     * @return 按类型分组的VMS路径Map，key为类型，value为对应类型的VMS路径列表
     * @throws RuntimeException 当获取资源路径失败时抛出
     */
    @Override
    public Map<String, List<VmsPathVo>> vmsResourcePathList() {
        try {
            // 1. 获取vms目录下所有相对路径文件列表
            List<String> vmsRelativeFiles = ResourceFileUtil.getRelativeFiles("vms");

            // 2. 处理文件路径并分组，将文件路径字符串转换为VmsPathVo对象
            return vmsRelativeFiles.parallelStream()
                    .map(vmFile -> {
                        // 分割文件路径
                        String[] filepathList = vmFile.split("/");

                        // 获取文件名（不含扩展名）
                        String filename = filepathList[filepathList.length - 1].replace(".vm", "");

                        /*
                          生成前端可用的唯一DOM元素ID
                          格式: "id-" + 无横线的UUID (例如: "id-550e8400e29b41d4a716446655440000")
                          用途:
                          1. 用于关联label标签和input元素的for属性
                          2. 确保列表项在前端有唯一标识
                         */
                        String id = "id-" + UUID.randomUUID().toString().replace("-", "");

                        return VmsPathVo.builder()
                                .id(id)
                                .name(vmFile)
                                .label(filename)
                                .type(filepathList[0])  // 使用路径的第一部分作为类型
                                .build();
                    })
                    // 转换为VO对象
                    .collect(Collectors.groupingBy(VmsPathVo::getType));  // 按类型分组
        } catch (Exception e) {
            throw new RuntimeException("Failed to get VMS resource paths: " + e.getMessage(), e);
        }
    }

}