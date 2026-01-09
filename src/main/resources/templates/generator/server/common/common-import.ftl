<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByLowercaseName" type="java.lang.String" -->

<#macro importPackage>
    import cn.hutool.core.bean.BeanUtil;
    import ${packagePath}.model.dto.${formatterTableNameByUppercaseName}QueryDTO;
    import ${packagePath}.model.dto.${formatterTableNameByUppercaseName}CreateDTO;
    import ${packagePath}.model.dto.${formatterTableNameByUppercaseName}UpdateDTO;
    import ${packagePath}.model.entity.${formatterTableNameByUppercaseName}Entity;
    import ${packagePath}.model.vo.${formatterTableNameByUppercaseName}VO;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import lombok.RequiredArgsConstructor;
    import ${packagePath}.service.${formatterTableNameByUppercaseName}Service;
    import com.auth.common.model.result.PageResult;
    import com.auth.common.model.result.Result;
    import com.auth.common.model.enums.ResultCodeEnum;
    import com.baomidou.mybatisplus.core.metadata.IPage;
    import org.apache.ibatis.annotations.Param;
    import com.baomidou.mybatisplus.extension.service.IService;
    import io.swagger.v3.oas.annotations.media.Schema;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.*;
    import com.auth.common.model.dto.BasePageDTO;

    import java.time.LocalDateTime;
    import java.math.BigDecimal;
    import java.util.Date;

    import jakarta.validation.constraints.NotNull;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Date;
    import java.math.BigDecimal;

</#macro>