/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.testsuite.integration.jaxrs.cdiintegration;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.testsuite.integration.common.HttpRequest;
import org.jboss.as.testsuite.integration.jaxrs.servletintegration.WebXml;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests injections of CDI beans into JAX-RS resources
 *
 * @author Stuart Douglas
 */
@RunWith(Arquillian.class)
@RunAsClient
@Ignore("[AS7-734] Migrate to ARQ Beta1")
public class CDIResourceInjectionTestCase {

    @Deployment(testable = false)
    public static Archive<?> deploy() {
        WebArchive war = ShrinkWrap.create(WebArchive.class,"jaxrsnoap.war");
        war.addPackage(HttpRequest.class.getPackage());
        war.addClasses(CDIResourceInjectionTestCase.class, CDIResource.class, CDIBean.class);
        war.addAsWebInfResource(WebXml.get("<servlet-mapping>\n" +
                "        <servlet-name>javax.ws.rs.core.Application</servlet-name>\n" +
                "        <url-pattern>/myjaxrs/*</url-pattern>\n" +
                "    </servlet-mapping>\n" +
                "\n"),"web.xml");
        return war;
    }


    private static String performCall(String urlPattern) throws Exception {
        return HttpRequest.get("http://localhost:8080/jaxrsnoap/" + urlPattern, 10, TimeUnit.SECONDS);
    }

    @Test
    public void testJaxRsWithNoApplication() throws Exception {
        String result = performCall("myjaxrs/cdiInject");
        assertEquals("Hello World!", result);
    }


}
