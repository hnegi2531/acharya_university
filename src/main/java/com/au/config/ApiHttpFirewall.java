package com.au.config;

import org.springframework.security.web.firewall.StrictHttpFirewall;




import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;


public final class ApiHttpFirewall extends StrictHttpFirewall{
	
	private static final Logger LOGGER = Logger.getLogger(ApiHttpFirewall.class.getName());

    
    public ApiHttpFirewall()
    {
        super();
        
    }

    
    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException
    {
        try
        {
        	this.inspect(request);
            return super.getFirewalledRequest(request);
        } catch (RequestRejectedException ex) {
            if (LOGGER.isLoggable(Level.WARNING))
            {
                LOGGER.log(Level.WARNING, "Intercepted RequestBlockedException: Remote Host: " + request.getRemoteHost() + " User Agent: " + request.getHeader("User-Agent") + " Request URL: " + request.getRequestURL().toString());
            }

            // Wrap in a new RequestRejectedException with request metadata and a shallower stack trace.
            throw new RequestRejectedException(ex.getMessage() +" " +request.getRequestURL().toString())
            {
                private static final long serialVersionUID = 1L;

                @Override
                public synchronized Throwable fillInStackTrace()
                {
                    //return this; // suppress the stack trace.
                    return this;
                }
            };
        }
    }
    
    public void inspect(HttpServletRequest request) throws RequestRejectedException
    {
        final String requestUri = request.getRequestURI(); // path without parameters
//        final String requestUrl = request.getRequestURL().toString(); // full path with parameters

        if (requestUri.endsWith("/api/authenticate"))
        {
            
        }else {
        	throw new RequestRejectedException("The request was rejected because it is a vulnerability scan.");
        }

        
    }


}
