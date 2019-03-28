package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.rest.MDCPopulationInterceptor;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Inject
	private OIDCRequestContextHolder oidcRequestContextHolder;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MDCPopulationInterceptor(oidcRequestContextHolder));
	}
}
