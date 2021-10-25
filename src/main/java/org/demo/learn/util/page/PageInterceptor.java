package org.demo.learn.util.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * mybaits 分页拦截器 （说明：拦截类型只能是接口）
 */
@Slf4j
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {
    private static final String DEFAULT_DIALECT = "MySQL"; // 数据库类型(默认为mysql)
    private static final String DEFAULT_PAGESQL_ID = ".*Page$"; // 需要拦截的ID(正则匹配)
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    private static final String dialect = DEFAULT_DIALECT;
    private static final String pageSqlId = DEFAULT_PAGESQL_ID;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler satementHandler = (StatementHandler) invocation.getTarget();

        MetaObject metaStatementHandler = MetaObject.forObject(satementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        //分离代理对象链（由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面两次循环可以分理处最原始的目标类）
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }

        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }

        MappedStatement mappedStatement = (MappedStatement)
                metaStatementHandler.getValue("delegate.mappedStatement");
        // 只重写需要分页的sql语句，通过mappedStatement的ID匹配。默认重写以Page结尾的sql
        if (mappedStatement.getId().matches(pageSqlId)) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (null == parameterObject) {
                throw new NullPointerException("parameterObject is null!");
            } else {
                //分页参数作为参数对象parameterObject的一个属性
                Page<?> page = (Page<?>) metaStatementHandler.getValue("delegate.boundSql.parameterObject.page");
                if (page.getPageSize() <= 0) return invocation.proceed();
                String sql = boundSql.getSql();
                //重写sql
                String pageSql = buildPageSql(sql, page);
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);

                //采用物理分页之后，就不需要mybatis的内存分页了，所系重置下面两个参数
                metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
                Connection connection = (Connection) invocation.getArgs()[0];
                // 重设分页参数里的总页数等
                setPageParameter(sql, connection, mappedStatement, boundSql, page);
            }

        }
        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    // sql重写
    private String buildPageSql(String sql, Page<?> page) {
        if (page != null) {
            StringBuilder pageSql;
            if ("mysql".equalsIgnoreCase(dialect)) {
                pageSql = buildPageSqlForMysql(sql, page);
            } else if ("oracle".equalsIgnoreCase(dialect)) {
                pageSql = buildPageSqlForOracle(sql, page);
            } else {
                return sql;
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    //Oracle分页实现
    private StringBuilder buildPageSqlForOracle(String sql, Page<?> page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf(page.getCurrentPage() - 1 * page.getPageSize());
        String endrow = String.valueOf(page.getCurrentPage() * page.getPageSize());
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <=").append(endrow);
        pageSql.append(") where row_id > ").append(beginrow);
        return pageSql;
    }

    // Mysql 分页实现
    private StringBuilder buildPageSqlForMysql(String sql, Page<?> page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginRow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
        pageSql.append(sql);
        pageSql.append(" limit " + beginRow + "," + page.getPageSize());
        return pageSql;
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用
     * 者就可用通过 分页参数<code>PageParameter</code>获得相关信息。
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, Connection connection,
                                  MappedStatement mappedStatement, BoundSql boundSql, Page<?> page) {
        // 记录总数
        String countSql = "select count(*) from (" + sql + ") as total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs != null) {
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            } else {
                log.error("error sql " + countSql);
            }
            page.setTotal(totalCount);
        }  catch (SQLException e) {
            log.error("Ignore this exception", e);
        }catch (Exception e) {
            log.error("exception:", e);
//            log.warn("mybatis Parameter '__frch_buildingId_0' not found!");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
            try {
                if (countStmt != null) {
                    countStmt.close();
                }
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
        }
    }

    /**
     * 对SQL参数(?)设值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps,
                               MappedStatement mappedStatement,
                               BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

}