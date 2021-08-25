package com.divisapp.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.*;
import org.springframework.util.MimeType;

public class MediaTypeConverter {

    private static final Map<String, MediaType> values = new HashMap<>();    
    static {
        values.put("application/pdf", APPLICATION_PDF);
        values.put("image/png", IMAGE_PNG);
        values.put("image/jpeg", IMAGE_JPEG);
        values.put("image/jpg", IMAGE_JPEG);
        values.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", APPLICATION_OCTET_STREAM);
    }
    
    public static MediaType getMediaType(String mime) {
        return values.get(mime);
    }

}
