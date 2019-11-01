package com.sharding.example;

import com.sharding.example.config.YamlConfigurationExample;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * qiqs
 */
public class YamlConfigTest {
    @Test
    public void testSelect() throws IOException, SQLException {
        DataSource dataSource = YamlConfigurationExample.getDataSource();
        Connection con = dataSource.getConnection();
        Statement st = null;
        ResultSet rs = null;

        String sql = "select * from t_order where order_id =11";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()){
                System.out.println("rs = "+rs.getString(1));
            }
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {

            }
        }
    }
}
