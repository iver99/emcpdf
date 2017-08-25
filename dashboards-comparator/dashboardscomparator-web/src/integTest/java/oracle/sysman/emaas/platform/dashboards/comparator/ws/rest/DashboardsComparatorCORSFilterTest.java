package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static oracle.sysman.emSDK.emaas.authz.listener.AuthorizationListener.OAM_REMOTE_USER_HEADER;
import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/10.
 */
@Test(groups={"s2"})
public class DashboardsComparatorCORSFilterTest {
    private DashboardsComparatorCORSFilter dashboardsComparatorCORSFilter = new DashboardsComparatorCORSFilter();
    @Mocked
    HttpServletResponse httpServletResponse;
    @Mocked
    HttpServletRequest httpServletRequest;
    @Mocked
    FilterChain chain;
    @Mocked
    FilterConfig filterConfig;
    @Test
    public void testDestroy() {
        dashboardsComparatorCORSFilter.destroy();
    }

    @Test
    public void testDoFilter() throws Exception {

        new Expectations(){
            {
                httpServletRequest.getHeader("Origin");
                result = "origin";
                httpServletRequest.getHeader("X-USER-IDENTITY-DOMAIN-NAME");
                result = "remote_user.user";
            }
        };
        dashboardsComparatorCORSFilter.doFilter(httpServletRequest, httpServletResponse, chain);
    }

    @Test
    public void testInit() throws ServletException {
        dashboardsComparatorCORSFilter.init(filterConfig);
    }
    

}