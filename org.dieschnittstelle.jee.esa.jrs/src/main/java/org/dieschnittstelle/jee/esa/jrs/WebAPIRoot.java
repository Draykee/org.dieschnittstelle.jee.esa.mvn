package org.dieschnittstelle.jee.esa.jrs;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class WebAPIRoot extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet(Arrays.asList(new Class[]{TouchpointCRUDServiceImpl.class,
                                                    TouchpointCRUDServiceImplAsync.class,
                                                    ProductCRUDServiceImpl.class,
                                                    JacksonJaxbJsonProvider.class}));
    }
}
