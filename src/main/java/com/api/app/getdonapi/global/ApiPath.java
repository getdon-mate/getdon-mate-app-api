package com.api.app.getdonapi.global;

public class ApiPath {
    public static final String API = "/api";

    public static final class Member {
        public static final String ROOT  = API + "/member";
        public static final String JOIN  = "/join";
        public static final String LOGIN = "/login";
    }

    public static final class Meeting {
        public static final String ROOT  = API + "/meeting";
        public static final String MY_LIST = "/my-list";
        public static final String CREATE  = "/create";
    }
}
