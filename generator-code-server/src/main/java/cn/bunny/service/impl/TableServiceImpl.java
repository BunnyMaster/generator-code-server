package cn.bunny.service.impl;

import cn.bunny.core.factory.ConcreteDatabaseInfoService;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final ConcreteDatabaseInfoService concreteDatabaseInfoService;

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    @SneakyThrows
    @Override
    public DatabaseInfoMetaData databaseInfoMetaData() {
        List<TableMetaData> databaseTableList = concreteDatabaseInfoService.databaseTableList(null);

        // 将当前数据库表分组，以数据库名称为key
        List<TableMetaData> databaseList = databaseTableList.stream()
                .collect(Collectors.groupingBy(TableMetaData::getTableCat))
                .values().stream()
                .map(tableInfoVos -> {
                    TableMetaData tableInfoVo = tableInfoVos.get(0);
                    tableInfoVo.setTableName(null);
                    return tableInfoVo;
                }).toList();

        DatabaseInfoMetaData databaseInfoMetaData = concreteDatabaseInfoService.databaseInfoMetaData();
        databaseInfoMetaData.setDatabaseList(databaseList);

        return databaseInfoMetaData;
    }
}
