package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.PrivilegeChecker;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chehao on 2017/1/12.
 */
@Test(groups = { "s2" })
public class UserInfoEntityTest {
    @Test
    public void testUserInfoEntity(){
        UserInfoEntity ui =new UserInfoEntity();

        ui.getCurrentUser();
        ui.getUserRoles();
    }

    @Test
    public void testUserInfoEntityWithUserRoles(@Mocked final PrivilegeChecker anyPrivilegeChecker){
        final List<String> userRoles = Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_ITA, PrivilegeChecker.ADMIN_ROLE_NAME_APM);
        UserInfoEntity ui =new UserInfoEntity(userRoles);

        new Expectations() {
            {
                PrivilegeChecker.getUserRoles(anyString, anyString);
                times = 0;
            }
        };
        ui.getUserRoles();
    }
}
