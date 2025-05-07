package com.rage.ecommerce.drools.infrastructure.config;

import org.drools.io.ClassPathResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Configuration
public class DroolsConfig {

    private static final String STANDARD_RULES_PATH = "rules/standard/";
    private static final String PREMIUM_RULES_PATH = "rules/premium/";
    public static final String STANDARD_SESSION_NAME = "standardKieSession";
    public static final String PREMIUM_SESSION_NAME = "premiumKieSession";

    private static final String STANDARD_RULES_PACKAGE = "com.rage.ecommerce.drools.rules.standard";
    private static final String PREMIUM_RULES_PACKAGE = "com.rage.ecommerce.drools.rules.premium";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();

        KieBaseModel standardKieBaseModel = kieModuleModel.newKieBaseModel("standardKieBase")
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM)
                .addPackage(STANDARD_RULES_PACKAGE);

        standardKieBaseModel.newKieSessionModel(STANDARD_SESSION_NAME)
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL);

        KieBaseModel premiumKieBaseModel = kieModuleModel.newKieBaseModel("premiumKieBase")
                .setDefault(false)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM)
                .addPackage(PREMIUM_RULES_PACKAGE);

        premiumKieBaseModel.newKieSessionModel(PREMIUM_SESSION_NAME)
                .setDefault(false)
                .setType(KieSessionModel.KieSessionType.STATEFUL);

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());

        addRulesFromDirectory(kieFileSystem, kieServices, STANDARD_RULES_PATH, STANDARD_RULES_PACKAGE, "standardKieBase");
        addRulesFromDirectory(kieFileSystem, kieServices, PREMIUM_RULES_PATH, PREMIUM_RULES_PACKAGE, "premiumKieBase");
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            StringBuilder errors = new StringBuilder();
            kieBuilder.getResults().getMessages(Message.Level.ERROR).forEach(
                    message -> errors.append("Error: ").append(message.getText()).append("\n")
            );
            throw new RuntimeException("Build Errors:\n" + errors.toString());
        }

        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }

    private void addRulesFromDirectory(KieFileSystem kieFileSystem, KieServices kieServices,
                                       String sourceDirectoryPath, String targetPackage, String kieBaseName) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:" + sourceDirectoryPath + "*.drl");

            String targetPathPrefix = targetPackage.replace(".", "/") + "/";

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                String fullPathInKieFileSystem = targetPathPrefix + fileName;

                kieFileSystem.write(ResourceFactory.newClassPathResource(sourceDirectoryPath + fileName, getClass())
                        .setResourceType(ResourceType.DRL)
                        .setSourcePath(fullPathInKieFileSystem));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading rule files from " + sourceDirectoryPath, e);
        }
    }
}
