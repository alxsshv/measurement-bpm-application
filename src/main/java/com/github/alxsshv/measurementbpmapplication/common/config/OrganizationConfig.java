package com.github.alxsshv.measurementbpmapplication.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "organization")
public record OrganizationConfig (
    String sign,
    String title
) {}
