package de.samply.lens_beacon_service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
    public static String toJson(Object object) {
        String jsonData = "{}";
        try {
            jsonData = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while processing JSON, check JSON syntax");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("An unknown error occurred while converting object into JSON");
            e.printStackTrace();
        }
        return jsonData;
    }

}
