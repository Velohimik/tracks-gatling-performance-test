package com.tracks.performance.enums;

public enum RequestBodyParameter {

    CONTEXT_NAME_PARAM("context[name]"),
    CONTEXT_STATE_PARAM("context_state[hide]"),
    AUTHENTICITY_TOKEN("authenticity_token");


    private final String parameter;

    RequestBodyParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
