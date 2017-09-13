/*
 * Copyright (c) 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
package oracle.sysman.emaas.platform.uifwk.nls.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.logging.log4j.LogManager;

public class NLSFilter implements Filter
{

    private static final Pattern pattern = Pattern.compile("lang=\"en(-US|-us)?\"");
    private static final String defaultLocale = "en-US";
    private static final String[] supportedLanguages = new String[]{"en", "fr", "ko", "zh-Hans", "zh-Hant", "zh"};
    private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(NLSFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            LOGGER.error("Error occurred in NLS filter!");
        }
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Chains with a wrapper which captures the response text
        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        chain.doFilter(request, wrapper);
        final String langAttr = NLSFilter.getLangAttr(httpRequest);


        final String responseText = wrapper.getResponseText();
        if(responseText == null){
            LOGGER.error("Response Text is null in NLS filter!");
            return;
        }
        final String newResponseText = pattern.matcher(responseText).replaceFirst(langAttr);

        // Writes the updated response text to the response object
        try (PrintWriter writer = response.getWriter()) {
            writer.println(newResponseText);
        }
    }

    public static String getLangAttr(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }
        // get the accept-language header
        //String alh = StringEscapeUtils.escapeHtml4(httpRequest.getHeader("Accept-Language"));
        Locale alh = httpRequest.getLocale();
        // calculate the UI locale
        String locale = getSupportedLocale(alh.toLanguageTag());

        // Replaces lang="en-US" by lang="xx" in response text
        return "lang=\"" + locale + "\"";
    }

    @Override
    public void destroy()
    {
    }
    
    private static String getSupportedLocale(String alh)
    {
        if (alh != null && !alh.isEmpty()) {
            String locale = alh.split(",")[0].split(";")[0];
            for (String lang : supportedLanguages) {
                if (locale.matches("^" + lang + "(-[A-Za-z]{2})?$")) {
                    return locale;
                }
            }
        }
        return defaultLocale;
    }

    private static class CaptureWrapper extends HttpServletResponseWrapper
    {
        private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        private final ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException
            {
                byteStream.write(b);
            }
        };
//        private final PrintWriter writer = 
//                new PrintWriter(new OutputStreamWriter(byteStream, Charset.forName("UTF-8").newEncoder()));
        private PrintWriter writer = null;
        {
          try {
            writer = new PrintWriter(new OutputStreamWriter(byteStream,"UTF-8"));
          } catch(UnsupportedEncodingException e) {
            final Logger logger = Logger.getLogger(NLSFilter.class.getSimpleName());
            logger.log(Level.SEVERE, "Error: Encoding UTF-8 is not supported.");
          }
        }
        
        public CaptureWrapper(HttpServletResponse response)
        {
            super(response);
        }

        public String getResponseText()
        {
            String result;
            final Logger logger = Logger.getLogger(NLSFilter.class.getSimpleName());
            try {
                outputStream.flush();
                result = byteStream.toString("UTF-8");
            } catch (IOException x) {
                logger.log(Level.SEVERE, "Failed to decode outputStream", x);
                result = null;
            } finally {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Unable to close outputStream", e);
                }
            }
            return result;
        }

        @Override
        public ServletOutputStream getOutputStream()
        {
            return outputStream;
        }

        @Override
        public PrintWriter getWriter()
        {
            return writer;
        }
    }
}
