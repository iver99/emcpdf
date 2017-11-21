package oracle.sysman.emaas.platform.dashboards.core.model;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;

@Test(groups = {"s2"})
public class DashboardTest {
    @Mocked
    DashboardServiceFacade dashboardServiceFacade;
    @Test
    public void testValueOf(@Mocked final EntityManager entityManager) throws Throwable {
        try {
            EmsDashboard from = new EmsDashboard();
            from.setCreationDate(new Date());
            from.setDashboardId(BigInteger.ONE);
            from.setDeleted(BigInteger.ZERO);
            from.setDescription("desc");
            from.setEnableDescription(1);
            from.setEnableTimeRange(1);
            from.setEnableRefresh(1);
            from.setEnableEntityFilter(1);
            from.setEnableDescription(1);
            from.setIsSystem(1);
            from.setShowInHome(1);
            from.setSharePublic(1);
            from.setLastModifiedBy("lastModifiedBy");
            from.setLastModificationDate(new Date());
            from.setName("name");
            from.setOwner("owner");
            from.setType(2);
            from.setExtendedOptions("exOptions");
            from.setApplicationType(1);
            from.setFederationSupported(1);
            List<EmsSubDashboard> emsSubDashboards = new ArrayList<>();
            final List<EmsDashboard> emsDashboards = new ArrayList<>();
            from.setSubDashboardList(emsSubDashboards);
            new Expectations() {
                {
                    dashboardServiceFacade.getEmsDashboardByIds((List) any, anyLong);
                    result = emsDashboards;
                    dashboardServiceFacade.getEntityManager();
                    result = entityManager;
                }
            };
            Dashboard.valueOf(from, new Dashboard(), true, true, true);
        } catch (Exception e) {
            //EMCPDF-3163
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            }
            throw e;
        }
    }
}