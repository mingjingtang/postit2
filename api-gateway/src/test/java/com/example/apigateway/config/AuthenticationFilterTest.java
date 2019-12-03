package com.example.apigateway.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.netflix.zuul.context.RequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationFilterTest {

  private AuthenticationFilter filter = new AuthenticationFilter();

  @Mock
  private DiscoveryClient discovery;

  private ZuulProperties properties = new ZuulProperties();

  private DiscoveryClientRouteLocator routeLocator;

  private MockHttpServletRequest request = new MockHttpServletRequest();

  private ProxyRequestHelper proxyRequestHelper = new ProxyRequestHelper();

  @Before
  public void init() {
    initMocks(this);
    this.properties = new ZuulProperties();
    this.proxyRequestHelper = new ProxyRequestHelper(properties);
    this.routeLocator = new DiscoveryClientRouteLocator("/", this.discovery, this.properties);
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.clear();
    ctx.setRequest(this.request);
  }

  @After
  public void clear() {
    RequestContext.getCurrentContext().clear();
    SecurityContextHolder.clearContext();
  }

  @Test
  public void basicProperties() throws Exception {
    assertEquals(1, filter.filterOrder());
    assertTrue(filter.shouldFilter());
    assertEquals("pre", filter.filterType());
  }

  @Test
  public void prefixRouteAddsHeader_Success() {
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
    this.filter.run();
    RequestContext ctx = RequestContext.getCurrentContext();
    assertNotNull(ctx.getZuulRequestHeaders().get("username"));
    assertEquals("user1", ctx.getZuulRequestHeaders().get("username"));
  }

  @Test()
  public void prefixRouteAddsHeader_NoAuthenticationInfo() {
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenThrow(NullPointerException.class);
    Object result = this.filter.run();
    assertNull(result);
  }
}
