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

import java.util.List;

import static java.util.concurrent.TimeUnit.DAYS;

@Configuration
@EnableCaching
public class CacheConfig {

	public static final String DOKMET_CACHE = "dokmetCache";

	@Bean
	@Primary
	@Profile({"nais", "local"})
	CacheManager cacheManager() {
		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(List.of(
				new CaffeineCache(DOKMET_CACHE, Caffeine.newBuilder()
						.expireAfterWrite(1, DAYS)
						.recordStats()
						.build())
				)
		);
		return manager;
	}
}
