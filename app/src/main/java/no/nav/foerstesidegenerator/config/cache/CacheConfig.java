package no.nav.foerstesidegenerator.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableCaching
public class CacheConfig {

	public static final String DOKMET_DOKUMENT_TYPE_INFO_CACHE = "DOKUMENT_TYPE_INFO";
	public static final String AZURE_CLIENT_CREDENTIAL_TOKEN_CACHE = "AZUREAD";

	@Bean
	@Primary
	@Profile({"nais", "local"})
	CacheManager cacheManager() {
		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(Arrays.asList(
				new CaffeineCache(AZURE_CLIENT_CREDENTIAL_TOKEN_CACHE, Caffeine.newBuilder()
						.expireAfterWrite(50, MINUTES).build()),
				new CaffeineCache(DOKMET_DOKUMENT_TYPE_INFO_CACHE, Caffeine.newBuilder()
						.expireAfterWrite(60, MINUTES).build())
		));
		return manager;
	}
}
