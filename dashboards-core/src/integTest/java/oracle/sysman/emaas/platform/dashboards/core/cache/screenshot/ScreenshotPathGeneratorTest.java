package oracle.sysman.emaas.platform.dashboards.core.cache.screenshot;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ScreenshotPathGeneratorTest
{
	@Test
	public void testValidFileName()
	{
		ScreenshotPathGenerator spg = ScreenshotPathGenerator.getInstance();
		Assert.assertTrue(spg.validFileName(1L, "123_1.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, "123_1.jpg", "11_1.jpg"));
		Assert.assertFalse(spg.validFileName(1L, "10_1.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, "123_3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, "12s_3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, "3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, "", "11_1.png"));
		Assert.assertFalse(spg.validFileName(1L, null, "11_1.png"));
	}
}
