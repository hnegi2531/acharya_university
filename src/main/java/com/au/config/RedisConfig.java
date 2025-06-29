package com.au.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

//@Configuration
//@EnableRedisRepositories
public class RedisConfig {
	
//	 @Value("${spring.redis.host}")
//	  private String redis_host;
//
//	  @Value("${spring.redis.port}")
//	  private int redis_port;
	  
//	  @Value("${spring.redis.password}")
//	  private String password;
	  
//	  @Autowired
//	  private CacheManager cacheManager;

//	  @Bean
//	  public LettuceConnectionFactory redisConnectionFactory() {
//	    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redis_host, redis_port);
//	    System.out.println("RedisStandaloneConfiguration");
//	    return new LettuceConnectionFactory(configuration);
//	  }
//	  
//	  @Bean
//	  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		 System.out.println("RedisCacheManager");
//	    return RedisCacheManager.create(connectionFactory);
//	  }
	  
//	  public void refreshCache(String cacheName) {
//	        Cache cache = cacheManager.getCache(cacheName);
//	        if (cache != null) {
//	            cache.clear(); // Clearing the cache
//	        }
//	    }
	  
//	  	@Bean
//	    @Primary
//	    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
//	        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//	                .useSsl().and()
//	                .commandTimeout(Duration.ofMillis(60000)).build();
//	        return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
//	    }
//
//	    @Bean
//	    public RedisConfiguration defaultRedisConfig() {
//	        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//	        config.setHostName(redis_host);
//	        config.setPassword(RedisPassword.of(password));
//	        return config;
//	    }
//	  
}