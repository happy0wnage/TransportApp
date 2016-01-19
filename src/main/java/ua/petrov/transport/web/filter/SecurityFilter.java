
package ua.petrov.transport.web.filter;

import ua.petrov.transport.core.JAXB.ParserUtil;
import ua.petrov.transport.core.JAXB.security.Rule;
import ua.petrov.transport.core.JAXB.security.Security;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.ParserPath;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Владислав on 01.08.2015.
 */


@WebFilter("/*")
public class SecurityFilter extends BaseHttpFilter {

    private static final String RESOURCES = "resources";
    private static final String COMMON = "common";
    private static final String USER = "user";

    private Set<String> userURIs;
    private Set<String> commonURIs;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        Security security = ParserUtil.unmarshal(Security.class, ParserPath.SECURITY_XML);
        for (Rule rule : security.getRules()) {
            if (rule.getRole().equals(COMMON)) {
                commonURIs = new HashSet<>(rule.getUrls());
            }
            if (rule.getRole().equals(USER)) {
                userURIs = new HashSet<>(rule.getUrls());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (accessGranted(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(Constants.INDEX);
        }
    }

    private boolean accessGranted(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        String requestURI = request.getServletPath();

        if (isResources(requestURI)) {
            return true;
        }

        User usersCurrent = (User) userSession.getAttribute(Entities.LOGGED_USER);

        if (usersCurrent == null) {
            return commonURIs.contains(requestURI);
        }

        return userURIs.contains(requestURI);
    }

    private boolean isResources(String URI) {
        return URI.contains(RESOURCES);
    }
}
