package oracle.sysman.emaas.platform.dashboards.testsdk;

import org.testng.annotations.Test;

import java.sql.*;

import org.testng.Assert;

/**
 * Created by chehao on 8/14/2017 11:38.
 */
@Test
public class InsertDBTest {

    @Test(groups = { "s2" })
    public void testInsert1() throws SQLException {
        Connection conn = DatabaseUtil.ConnectCloud1();//this c is null
//        Assert.assertNotNull(conn);//failed
        PreparedStatement pstmt = null;
        String sql = "insert into ems_dashboard values(?,'Test dashboard',0,CONCAT('Test dashboard description',?),'18-JUL-17 05.09.38.583000000 AM','18-JUL-17 05.09.38.583000000 AM','Oracle','Oracle',0,3,1,'screenshot',0,?,1,0,1,0,null,1)";
        System.out.println(sql);
        try {
            for(int i = 0; i < 100 ; i++){
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                pstmt.setLong(1, 5000000+i);//dashboard id
                pstmt.setLong(2, 5000000+i);//dashboard id
                pstmt.setLong(3, 673850132);//tenant id
                int j = pstmt.executeUpdate();
//                System.out.println("i value is " + j);
                Assert.assertNotEquals(j,0);
                pstmt.close();
            }
            conn.close();
        }catch(SQLException  e){
            e.printStackTrace();
            System.out.println(e);
        }

    }


    @Test(groups = { "s2" })
    public void testInsert2() throws SQLException {
        Connection conn = DatabaseUtil.ConnectCloud1();//this c is null
//        Assert.assertNotNull(conn);//failed
        PreparedStatement pstmt = null;
        String sql = "insert into ems_dashboard values(?,'Test dashboard',0,CONCAT('Test dashboard description',?),'18-JUL-17 05.09.38.583000000 AM','18-JUL-17 06.09.38.583000000 AM','Oracle','Oracle',0,3,1,'screenshot',0,?,1,0,1,0,null,1)";
        System.out.println(sql);
        try {
            for(int i = 0; i < 100 ; i++){
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                pstmt.setLong(1, 6000000+i);
                pstmt.setLong(2, 6000000+i);
                pstmt.setLong(3, 673850132);
                int j = pstmt.executeUpdate();
//                System.out.println("i value is " + j);
                Assert.assertNotEquals(j,0);
                pstmt.close();
            }
            conn.close();
        }catch(SQLException  e){
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
