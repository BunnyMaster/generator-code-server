package com.auth.module.generator.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(name = "SupportEnumsVO", description = "支持生成的枚举")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportEnumsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "支持生成的类型")
    private List<ElementOptionVO> supportProvider;

    @Schema(description = "支持生成内容")
    private List<ElementOptionVO> supportModes;
}
