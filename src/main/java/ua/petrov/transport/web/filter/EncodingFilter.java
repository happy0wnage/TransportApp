package ua.petrov.transport.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebFilter(urlPatterns = "/*",
        initParams = {@WebInitParam(name = EncodingFilter.ENCODING, value = "UTF-8")})
public class EncodingFilter extends BaseHttpFilter {

    private static final String UTF_8 = "UTF-8";
    static final String ENCODING = "encoding";
    private String encoding;

    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingFilter.class);

    public void init(FilterConfig fConfig) throws ServletException {
        encoding = fConfig.getInitParameter(ENCODING);
        if (encoding == null) {
            encoding = UTF_8;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        setReqEncoding(httpRequest);
        chain.doFilter(httpRequest, response);
    }

    private void setReqEncoding(HttpServletRequest request) throws UnsupportedEncodingException {
        String requestEncoding = request.getCharacterEncoding();

        if (requestEncoding == null) {
            LOGGER.trace("Request encoding = null, set encoding --> " + encoding);
            request.setCharacterEncoding(encoding);
        }
    }

}
