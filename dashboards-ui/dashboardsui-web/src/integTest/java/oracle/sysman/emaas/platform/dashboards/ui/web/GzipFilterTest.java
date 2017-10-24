package oracle.sysman.emaas.platform.dashboards.ui.web;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

public class GzipFilterTest {
  @Test(groups = { "s2" })
  public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest request,
			@Mocked final HttpServletResponse response) throws Exception {
	  GzipFilter filter = new GzipFilter();
	  
	  new Expectations() {
		  {
			  request.getHeader(anyString);
			  returns("xxx.gzip", //1
					  "xxx");	//2
		  }
	  };
	  
	  filter.doFilter(request, response, chain);	//1
	  filter.doFilter(request, response, chain);	//2
	  
	  new Verifications() {
		  {
			  chain.doFilter(request, response); 
			  times = 1;
		  }
	  };
  }
  @Test(groups = {"s2"})
  public void testInitDestroy(@Mocked FilterConfig arg0) throws ServletException{
	  GzipFilter filter = new GzipFilter();
	  filter.init(arg0);
	  filter.destroy();
  }
}
