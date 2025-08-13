package com.code.generator.service;

import com.code.generator.model.vo.VmsPathVo;

import java.util.List;
import java.util.Map;

public interface VmsService {

    /**
     * 获取vms文件路径
     *
     * @return vms下的文件路径
     */
    Map<String, List<VmsPathVo>> vmsResourcePathList();

}
