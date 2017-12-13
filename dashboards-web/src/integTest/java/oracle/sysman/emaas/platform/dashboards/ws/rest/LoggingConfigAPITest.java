package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.*;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.UpdatedLoggerLevel;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class LoggingConfigAPITest {

    @Mocked
    APIBase apiBase;

    LoggingConfigAPI loggingConfigAPI;

    @BeforeMethod
    public void setUp() {
        loggingConfigAPI = new LoggingConfigAPI();
        new NonStrictExpectations() {
            {
                apiBase.buildErrorResponse((ErrorEntity) any);
                result = null;
            }
        };
    }

    @Test
    public void testChangeSpecificLoggerLevelwithDashboardException() {
        Assert.assertNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", "", new JSONObject()));
    }

    @Test
    public void testChangeSpecificLoggerLevelwithIOException() {

        new MockUp<JsonUtil>() {
            @Mock
            public <T> T fromJson(String jsonString, Class<T> clazz) throws IOException{
                throw new IOException("jsonUtil - io exception");
            }
        };

        Assert.assertNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", "", new JSONObject()));
    }

    @Test
    public void testChangeSpecificLoggerLevel(@Mocked final LogUtil logUtil,
                                              @Mocked final LoggerContext loggerContext, @Mocked final LoggerConfig loggerConfig,
                                              @Mocked final LogManager logManager, @Mocked final UpdatedLoggerLevel updatedLoggerLevel,
                                              @Mocked final Level level, @Mocked final Configuration configuration) throws IOException {

        final String loggerName = "loggerName";
        final Map<String, LoggerConfig> LOGGERs = new HashMap<>();
        LOGGERs.put("1", loggerConfig);
        new Expectations() {
            {

                apiBase.getJsonUtil().fromJson(anyString, UpdatedLoggerLevel.class);
                result = updatedLoggerLevel;

                updatedLoggerLevel.getLevel();
                result = "DEBUG";

                Level.getLevel((String)any);
                result = level;

                LogManager.getContext(false);
                result = loggerContext;

                loggerContext.getConfiguration();
                result = configuration;

                configuration.getLoggers();
                result = LOGGERs;

                LogUtil.setLoggerUpdateTime((Configuration)any, (LoggerConfig)any, anyLong);
                loggerConfig.getName();
                result = "loggerName";

                loggerConfig.setLevel(level);

            }
        };
        Assert.assertNotNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", loggerName, new JSONObject()));
    }

    @Test
    public void testChangeRootLoggerLevel() {
        loggingConfigAPI.changeRootLoggerLevel("tenantIdParam", "userTenant",new JSONObject());
    }


    @Test
    public void testGetAllLoggerLevels(@Mocked final Level level, @Mocked final DefaultConfiguration configuration)  {
        final String loggerName = "loggerName";
        new Expectations() {
            {

                Map<String,LoggerConfig> loggers = new HashMap<>();
                loggers.put(loggerName,new LoggerConfig(loggerName, level, true));

               /* configuration.getLoggers();
                result = loggers;

                configuration.getProperties();
                result  = new HashMap<String,String>();*/

            }
        };
        Assert.assertNotNull(loggingConfigAPI.getAllLoggerLevels("tenantIdParam","userTenant"));

        Assert.assertNotNull(loggingConfigAPI.getAllLoggerLevels(null,"userTenant"));
    }
}
