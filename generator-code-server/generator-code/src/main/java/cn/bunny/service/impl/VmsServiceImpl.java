package cn.bunny.service.impl;

import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.vo.GeneratorVo;
import cn.bunny.domain.vo.VmsPathVo;
import cn.bunny.service.VmsService;
import cn.bunny.service.impl.vms.VmsCodeGeneratorService;
import cn.bunny.service.impl.vms.VmsZipService;
import cn.bunny.utils.ResourceFileUtil;
import cn.bunny.utils.VmsUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VMS服务主实现类，负责协调各子服务完成代码生成、资源管理和打包下载功能
 */
@Service
@RequiredArgsConstructor
public class VmsServiceImpl implements VmsService {

    private final VmsCodeGeneratorService codeGeneratorService;
    private final VmsZipService zipService;

    @Override
    public Map<String, List<GeneratorVo>> generator(VmsArgumentDto dto) {
        return codeGeneratorService.generateCode(dto);
    }

    @SneakyThrows
    @Override
    public Map<String, List<VmsPathVo>> vmsResourcePathList() {
        List<String> vmsRelativeFiles = ResourceFileUtil.getRelativeFiles("vms");

        return vmsRelativeFiles.stream()
                .map(vmFile -> {
                    String[] filepathList = vmFile.split("/");
                    String filename = filepathList[filepathList.length - 1].replace(".vm", "");

                    return VmsPathVo.builder()
                            .id(VmsUtil.generateDivId())
                            .name(vmFile)
                            .label(filename)
                            .type(filepathList[0])
                            .build();
                })
                .collect(Collectors.groupingBy(VmsPathVo::getType));
    }

    @Override
    public ResponseEntity<byte[]> downloadByZip(VmsArgumentDto dto) {
        byte[] zipBytes = zipService.createZipFile(dto);

        // 下载文件名称
        long currentTimeMillis = System.currentTimeMillis();
        String digestHex = MD5.create().digestHex(currentTimeMillis + "");
        String generateZipFilename = digestHex + ".zip";

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + generateZipFilename);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }
}