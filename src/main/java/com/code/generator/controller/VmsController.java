package com.code.generator.controller;

import com.code.generator.model.result.Result;
import com.code.generator.model.vo.VmsPathVo;
import com.code.generator.service.VmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "生成器", description = "代码生成器接口")
@RestController
@RequestMapping("/api/vms")
public class VmsController {

    @Resource
    private VmsService vmsService;

    @Operation(summary = "获取vms文件路径", description = "获取所有vms下的文件路径")
    @GetMapping("vmsResourcePathList")
    public Result<Map<String, List<VmsPathVo>>> vmsResourcePathList() {
        Map<String, List<VmsPathVo>> list = vmsService.vmsResourcePathList();
        return Result.success(list);
    }

}
