package com.sharding;

import com.sharding.example.config.YamlConfigurationExample;
import io.shardingsphere.api.HintManager;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class ShardingTestMain {
    /**
     * insert
     *
     * @throws SQLException
     */
    @Test
    public void testInsert() throws SQLException, IOException {
        Connection connection = YamlConfigurationExample.getDataSource().getConnection();
        for (int i = 0; i < 10; i++) {
            // String sql = "insert into t_order(order_id,user_id)values(1,1)";
            String sql = "insert into t_order(order_id,order_name,price)values(" + i + "," + i + "," + i + ")";
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
    }

    @Test
    public void testUpdateBlob() throws SQLException, IOException {
        Connection connection = YamlConfigurationExample.getDataSource().getConnection();
        String sql = "update t_order set user_id= ? , uname= ? where order_id= ? ";
        // String sql = "insert into t_order_item(item_id,order_id,user_id)values(" + i + "," + i + ",2)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "111");
        statement.setString(2, "dddd");
        statement.setInt(3, 10);
        statement.execute();
    }

    /**
     * select 查询
     *
     * @throws SQLException
     */
    @Test
    public void testselect() throws SQLException, IOException {
        Connection connection = YamlConfigurationExample.getDataSource().getConnection();
        // String sql = "select * from globalTable";
        String sql = "select * from  t_order a left join t_order_item b on a.order_name = b.order_name";
        Statement ps = connection.createStatement();
//            ps.setFetchSize(1);
        ResultSet rs = ps.executeQuery(sql);

        System.out.println("查询完成！");
//                TimeUnit.SECONDS.sleep(80);
        while (rs.next()) {
            System.out.println("======== : " + rs.getString(1));
        }
    }

    /**
     * hint强制路由
     *
     * @throws Exception
     */
    @Test
    public void testhint() throws Exception {
        Connection connection = ShardingJdbcDemo.getConn();
        HintManager hintManager = HintManager.getInstance();
        String sql = "select * from t_order";
        PreparedStatement ps = connection.prepareStatement(sql);
        hintManager.addDatabaseShardingValue("t_order", 0);
        hintManager.addTableShardingValue("t_order", 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("======== : " + rs.getString(1));
        }
    }

    @Test
    public void testselectFiectSize() throws SQLException {
        Connection connection = ShardingJdbcDemo.getConn();
        for (int i = 0; i < 1; i++) {
            int k = 0;
            String sql = "select * from t_order";
            Statement st = connection.createStatement();
            st.setFetchSize(10);
            st.setFetchDirection(ResultSet.FETCH_REVERSE);
            ResultSet rs = st.executeQuery(sql);
            System.out.println("执行完成。。。。。");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (rs.next()) {
                k++;
                System.out.println("======== : " + rs.getString(1));
            }
            System.out.println("查询条数 ： " + k);
        }
    }

    @Test
    public void testselectDis() throws SQLException, IOException {
        Connection connection = YamlConfigurationExample.getDataSource().getConnection();

        String sql = "SELECT * FROM PT_RESOURCE\n" +
                "WHERE 1=1\n" +
                "AND PT_RESOURCE.ISMENU = 1\n" +
                "AND PT_RESOURCE.STATUS = 1\n" +
                "AND PT_RESOURCE.RESOURCE_ID IN(\n" +
                "\tSELECT DISTINCT\n" +
                "\tPT_ROLE_RESOURCE.RESOURCE_ID\n" +
                "\tFROM \n" +
                "\tPT_ROLE\n" +
                "\tJOIN PT_ROLE_RESOURCE ON PT_ROLE_RESOURCE.ROLE_ID = PT_ROLE.ROLE_ID\n" +
                "\tAND PT_ROLE_RESOURCE.ROLE_ID IN(\n" +
                "\t\tSELECT ROLE_ID \n" +
                "\t\tFROM PT_ROLE\n" +
                "\t\tWHERE \n" +
                "\t\tROLE_CODE IN(1)\n" +
                "\t)\n" +
                ")";
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("ds_0");

        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            System.out.println("======== : " + rs.getString(1));

        }
    }

    @Test
    public void testShardingInsert() {
        try {
            Connection connection = YamlConfigurationExample.getDataSource().getConnection();
            ;
            // String sql = "insert into t_order(order_id,user_id)values(4,4)";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from t_order where order_id=2");
            while (rs.next()) {
                System.out.println(" order_id =" + rs.getString(1));
            }

            //st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintHintSimpleSelect() throws SQLException, IOException {
        String sql = "select distinct  order_id from t_order ";
        HintManager hintManager = HintManager.getInstance();
        Connection conn = YamlConfigurationExample.getDataSource().getConnection();
        hintManager.setDatabaseShardingValue("ds_0");
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            System.out.println("======== : " + rs.getString(1));

        }
    }
}
