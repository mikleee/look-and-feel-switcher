package com.aimprosoft.look_and_feel_switcher.model.view;

/**
 * crated by m.tkachenko on 05.10.15 17:43
 */
public class JsonResponse<T> {

    private String status;
    private T body;

    private JsonResponse(T body, String status) {
        this.body = body;
        this.status = status;
    }

    public static <T> JsonResponse<T> success(T body) {
        return new JsonResponse<T>(body, "success");
    }

    public static <T> JsonResponse<T> error(T body) {
        return new JsonResponse<T>(body, "error");
    }

    public T getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

}