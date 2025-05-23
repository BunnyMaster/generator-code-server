package cn.bunny.controller;

import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.result.Result;
import cn.bunny.domain.vo.GeneratorVo;
import cn.bunny.domain.vo.VmsPathVo;
import cn.bunny.service.VmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "生成代码", description = "生成代码")
    @PostMapping("generator")
    public Result<List<GeneratorVo>> generator(@Valid @RequestBody VmsArgumentDto dto) {
        List<GeneratorVo> list = vmsService.generator(dto);
        return Result.success(list);
    }

    @Operation(summary = "打包成zip下载", description = "打包成zip下载")
    @PostMapping("downloadByZip")
    public ResponseEntity<byte[]> downloadByZip(@Valid @RequestBody VmsArgumentDto dto) {
        return vmsService.downloadByZip(dto);
    }
}
