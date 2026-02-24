package no.nav.foerstesidegenerator.itest.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static java.util.concurrent.TimeUnit.MINUTES;
import static no.nav.foerstesidegenerator.config.cache.CacheConfig.DOKMET_CACHE;

@EnableCaching
@Profile("itest")
@TestConfiguration
public class LocalTestCacheConfig {

	@Bean
	@Primary
	CacheManager cacheManager() {
		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(List.of(
				new CaffeineCache(DOKMET_CACHE, Caffeine.newBuilder()
						.expireAfterWrite(0, MINUTES)
						.maximumSize(0)
						.build())
				)
		);
		return manager;
	}
}
