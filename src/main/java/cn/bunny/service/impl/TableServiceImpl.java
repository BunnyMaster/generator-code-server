package cn.bunny.service.impl;

import cn.bunny.core.provider.DatabaseMetadataProvider;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final DatabaseMetadataProvider databaseMetadataProvider;

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    @Override
    public DatabaseInfoMetaData databaseInfoMetaData() {
        List<TableMetaData> databaseTableList = databaseMetadataProvider.getTableMetadataBatch(null);

        // 将当前数据库表分组，以数据库名称为key
        List<TableMetaData> databaseList = databaseTableList.stream()
                .collect(Collectors.groupingBy(TableMetaData::getTableCat))
                .values().stream()
                .map(tableInfoVos -> {
                    TableMetaData tableInfoVo = tableInfoVos.get(0);
                    tableInfoVo.setTableName(null);
                    return tableInfoVo;
                })
                .toList();

        DatabaseInfoMetaData databaseInfoMetaData = databaseMetadataProvider.databaseInfoMetaData();
        databaseInfoMetaData.setDatabaseList(databaseList);

        return databaseInfoMetaData;
    }
}
