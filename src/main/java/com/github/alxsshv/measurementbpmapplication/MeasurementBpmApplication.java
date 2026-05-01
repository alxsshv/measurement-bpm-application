package com.github.alxsshv.measurementbpmapplication;

import com.github.alxsshv.measurementbpmapplication.common.config.OrganizationConfig;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({PathsConfig.class, OrganizationConfig.class})
@SpringBootApplication
public class MeasurementBpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeasurementBpmApplication.class, args);
	}

}
