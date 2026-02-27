package com.aristowebapi.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.Map;

public class JsonOnlyResponse {

    private Map<String, Object> data;

    public JsonOnlyResponse(Map<String, Object> data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }
}