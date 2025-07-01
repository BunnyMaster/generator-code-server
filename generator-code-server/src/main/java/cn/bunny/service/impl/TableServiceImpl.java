package cn.bunny.service.impl;

import cn.bunny.core.factory.ConcreteDatabaseInfo;
import cn.bunny.domain.entity.ColumnMetaData;
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

    private final ConcreteDatabaseInfo databaseInfoCore;

    /**
     * 获取表属性
     *
     * @param tableName 表名称
     * @return 表属性
     */
    @Override
    public TableMetaData tableMetaData(String tableName) {
        return databaseInfoCore.getTableMetadata(tableName);
    }

    /**
     * 获取[当前/所有]数据库表
     *
     * @return 所有表信息
     */
    @Override
    public List<TableMetaData> databaseTableList(String dbName) {
        return databaseInfoCore.databaseTableList(dbName);
    }

    /**
     * 获取当前表的列属性
     *
     * @param tableName 表名称
     * @return 当前表所有的列内容
     */
    @Override
    public List<ColumnMetaData> tableColumnInfo(String tableName) {
        return databaseInfoCore.tableColumnInfo(tableName);
    }

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    @SneakyThrows
    @Override
    public DatabaseInfoMetaData databaseInfoMetaData() {
        List<TableMetaData> databaseTableList = databaseTableList(null);

        // 将当前数据库表分组，以数据库名称为key
        List<TableMetaData> databaseList = databaseTableList.stream()
                .collect(Collectors.groupingBy(TableMetaData::getTableCat))
                .values().stream()
                .map(tableInfoVos -> {
                    TableMetaData tableInfoVo = tableInfoVos.get(0);
                    tableInfoVo.setTableName(null);
                    return tableInfoVo;
                }).toList();

        DatabaseInfoMetaData databaseInfoMetaData = databaseInfoCore.databaseInfoMetaData();
        databaseInfoMetaData.setDatabaseList(databaseList);

        return databaseInfoMetaData;
    }
}
