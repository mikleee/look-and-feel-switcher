package com.aimprosoft.look_and_feel_switcher.model.view;

import java.util.HashMap;
import java.util.Map;

/**
 * crated by m.tkachenko on 05.10.15 17:43
 */
public class JsonResponse {

    private String status;
    private Map<String, Object> body = new HashMap<String, Object>();
    private Object meta;

    private JsonResponse(String status) {
        this.status = status;
    }

    public static JsonResponse success() {
        return new JsonResponse("success");
    }

    public static JsonResponse error(String error) {
        return new JsonResponse("error").put("error", error);
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

    public Object getMeta() {
        return meta;
    }

    public JsonResponse setMeta(Object meta) {
        this.meta = meta;
        return this;
    }

    public JsonResponse put(String key, Object value) {
        body.put(key, value);
        return this;
    }

}