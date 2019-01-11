package com.viktor.timeofftests.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Properties;

public class LogConfigurer {

    public static void configureLogger(String dirPath){
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");

        AppenderComponentBuilder file = builder.newAppender("logfile","File");
        String fileName = dirPath + "qa_log.log";
        file.addAttribute("fileName", fileName);

        LayoutComponentBuilder layout = builder.newLayout("PatternLayout");
        layout.addAttribute("pattern","[%level]::[%c]::[%d{DEFAULT_MICROS}]::%m%n");
        console.add(layout);
        file.add(layout);

        LoggerComponentBuilder fileLogger = builder.newLogger("com.viktor.timeofftests", Level.DEBUG);
        fileLogger.add(builder.newAppenderRef("logfile"));

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
        rootLogger.add(builder.newAppenderRef("stdout"));
        builder.add(rootLogger);
        builder.add(console);
        builder.add(file);
        builder.add(fileLogger);
        Configurator.initialize(builder.build());
    }
}
