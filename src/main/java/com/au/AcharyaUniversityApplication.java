package com.au;


import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.au.controller.RateLimitController;
import com.au.event.BioMetricTransactionEventMonthWise;
import com.au.event.BioMetricTransactionMonthWiseEventPublisher;
import com.au.model.UserAuthentication;
import com.au.response.ResponseHandler;
import com.au.scheduler.EmployeeAttendanceScheduler;
import com.au.scheduler.LeaveKittyScheduler;
import com.au.service.UserCreationService;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RestController
@Configuration
//@EnableScheduling
@EnableCaching
@EnableAsync
@EnableConfigurationProperties
public class AcharyaUniversityApplication implements CommandLineRunner {

    @Autowired
    private UserCreationService userCreationService;

    @Autowired
    private EmployeeAttendanceScheduler employeeAttendanceScheduler;

    @Autowired
    private BioMetricTransactionMonthWiseEventPublisher bioMetricTransactionMonthWiseEventPublisher;

    @Autowired
    private LeaveKittyScheduler leaveKittyScheduler;


    //@Autowired
//	private static RateLimitController rateLimit;

    public static void main(String[] args) {
        SpringApplication.run(AcharyaUniversityApplication.class, args);


        RateLimitController.bucketInitialization();

//		RedisClient redisClient = new RedisClient(
//	      RedisURI.create("redis://clustercfg.acharya-redis-cache.vpkqi7.aps1.cache.amazonaws.com:6379"));
//	    RedisConnection<String, String> connection = redisClient.connect();
//	    
//	    System.out.println("Connected to Redis");	    
//	    connection.close();
//	    redisClient.shutdown();
    }

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @PostMapping("/UserCreation")
    public ResponseEntity<Object> saveUserAuthentication(@RequestBody @Valid UserAuthentication u) {
        UserAuthentication user = userCreationService.saveUserAuthentication(u);
        ResponseEntity<Object> userResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, user);
        return userResponse;
    }


    /*
     * @Bean public WebMvcConfigurer corsConfigurer() { return new
     * WebMvcConfigurer() {
     *
     * @Override public void addCorsMappings(CorsRegistry registry) {
     * registry.addMapping("/api/**").allowedOrigins("http://localhost:8080"); } };
     * }
     */

    //FOR CROSS ORIGIN(CORS)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }


    @Bean
    public Docket swaggerconfiguration1() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("student").select().paths(PathSelectors.ant("/api/student/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration2() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("employee").select().paths(PathSelectors.ant("/api/employee/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration3() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("academic").select().paths(PathSelectors.ant("/api/academic/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration4() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("institute").select().paths(PathSelectors.ant("/api/institute/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration5() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("inventory").select().paths(PathSelectors.ant("/api/inventory/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration6() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("hostel").select().paths(PathSelectors.ant("/api/hostel/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration7() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("proctor").select().paths(PathSelectors.ant("/api/proctor/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration8() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("feedback").select().paths(PathSelectors.ant("/api/feedback/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration9() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("finance").select().paths(PathSelectors.ant("/api/finance/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Bean
    public Docket swaggerconfiguration10() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("reports").select().paths(PathSelectors.ant("/api/reports/**"))
                .apis(RequestHandlerSelectors.basePackage("com.au.controller")).build();
    }

    @Override
    public void run(String... args) throws Exception {
        // NOTE: The following block of code is intended to run only in the production environment (Kubernetes CronJob).
        // Please comment out in local and staging environments to avoid unintended execution during development and testing.
        String runTask = System.getenv("RUN_TASK");

        if (runTask != null) {
            if (runTask.equalsIgnoreCase("empSheetJob")) {
                employeeAttendanceScheduler.bioTransactionScheduler();
            } else if (runTask.equalsIgnoreCase("bioTransJob")) {
                LocalDate localDate = LocalDate.now();
                Integer month = localDate.getMonthValue();
                Integer year = localDate.getYear();
                BioMetricTransactionEventMonthWise bioMetricTransactionEventMonthWise = new BioMetricTransactionEventMonthWise(month, year, null);
                bioMetricTransactionMonthWiseEventPublisher.handleBioTransactionEventEventPublisher(bioMetricTransactionEventMonthWise);
            } else if (runTask.equalsIgnoreCase("leaveKittyJob")) {
                leaveKittyScheduler.generateLeaveKitty();
            }
        } else {
            System.out.println("Skipping the Attendance Scheduler as it will run on kubernetes cronjob environment.");
        }
    }
}
