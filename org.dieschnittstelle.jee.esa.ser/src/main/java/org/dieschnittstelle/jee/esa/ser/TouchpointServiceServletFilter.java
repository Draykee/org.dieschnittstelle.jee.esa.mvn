package org.dieschnittstelle.jee.esa.ser;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TouchpointServiceServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // check whether we have a an accept-language header that will be set by the browser
        String acceptLanguageHeader = ((HttpServletRequest) request)
                .getHeader("accept-language");

        // allow access for non browser clients
        if (acceptLanguageHeader == null) {
            chain.doFilter(request, response);
        } else {
            // block access for browsers
            ((HttpServletResponse) response)
                    .setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {

    }
}
