package com.tracks.performance.enums;

public enum RequestBodyParameter {

    CONTEXT_NAME_PARAM("context[name]"),
    CONTEXT_STATE_PARAM("context_state[hide]"),
    AUTHENTICITY_TOKEN("authenticity_token"),
    USE_USER_LOGIN("user_login"),
    USE_USER_PASSWORD("user_password"),
    CREATE_USER_LOGIN("user[login]"),
    CREATE_USER_PASSWORD("user[password]"),
    CREATE_USER_PASSWORD_CONFIRM("user[password_confirmation]");


    private final String parameter;

    RequestBodyParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
