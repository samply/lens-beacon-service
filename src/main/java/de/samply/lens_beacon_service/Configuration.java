package de.samply.lens_beacon_service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Environment configuration parameters. */
@Data
@Component
public class Configuration {
    @Value("${proxy.url}")
    private String proxyUrl;

    @Value("${proxy.port}")
    private String proxyPort;

    @Value("${proxy.apikey}")
    private String proxyApiKey;
}
