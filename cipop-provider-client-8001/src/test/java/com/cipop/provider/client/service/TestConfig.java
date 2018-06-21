package com.cipop.provider.client.service;

import com.cipop.base.config.LocaleConfiguration;
import com.cipop.base.specification.RSQLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import javax.persistence.EntityManager;

/**
 * Created by longweiquan on 10/12/2016.
 */
@ComponentScan(basePackages = {"com.cipop.bean", "com.cipop.base", "com.cipop.provider.client"})
@Configuration
@EnableConfigurationProperties
@TestExecutionListeners(listeners = {
        WithSecurityContextTestExecutionListener.class
})
@Import(LocaleConfiguration.class)
public class TestConfig {

//    @Bean
//    public TokenProvider tokenProvider(){
//        return new TokenProvider();
//    }

    @Autowired(required = false)
    private EntityManager entityManager;

    @Bean
    public RSQLBuilder rsqlBuilder() {
        RSQLBuilder rsqlBuilder = new RSQLBuilder();
        rsqlBuilder.setEntityManager(entityManager);
        return rsqlBuilder;
    }

//    @Bean
//    public AuthorityManager authorityManager(){
//        return Mockito.mock(AuthorityManager.class);
//    }
//
//    @Value("${spring.data.mongodb.uri}")
//    private String uri;
//
//    @Bean(name = "clearanceMongo")
//    public MongoTemplate clearanceMongoTemplate() {
//        MongoClientURI mongoClientURI = new MongoClientURI(this.uri);
//        return new MongoTemplate(new SimpleMongoDbFactory(mongoClientURI));
//    }

//    @Bean
//    public JavaMailSender mailSender(){
//        return Mockito.mock(JavaMailSender.class);
//    }

//    @Bean
//    public AuthenticationManager authManager(){
//        return Mockito.mock(AuthenticationManager.class);
//    }

//    @Bean
//    public ClearanceRepository clearanceRepository(){
//        return Mockito.mock(ClearanceRepository.class);
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
//        return templateEngine;
//    }
//    @Bean
//    public ThymeleafViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        return viewResolver;
//    }

//    @Bean
//    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
//        SpringResourceTemplateResolver templateResolver
//            = new SpringResourceTemplateResolver();
//        templateResolver.setPrefix("classpath:/email/");
//        templateResolver.setSuffix(".html");
//        return templateResolver;
//    }

//    @Bean
//    public OMSPasswordEncoder OMSPasswordEncoder() {
//        return new OMSPasswordEncoder();
//    }

//    @Bean
//    public BasicAWSCredentials basicAWSCredentials() {
//        return new BasicAWSCredentials("AKIAINTR6GV7DGSQ5SQQ", "MDO+9W2qoD6ZFI4DNcUrZCfLLAFuVyxdfeR1xEHA");
//    }

//    @Bean
//    public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
//        AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
//        amazonS3Client.setRegion(Region.getRegion(Regions.fromName("eu-west-1")));
//        return amazonS3Client;
//    }

//    @Bean
//    public RestServiceRegistry serverlessRegistry(){
//        return new RestServiceRegistry();
//    }
}
