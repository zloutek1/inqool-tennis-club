package cz.inqool.tennis_club_reservation_system.configs;

public abstract class ApiUris {

    public static final String ROOT_URI = "/api/v1";

    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REFRESH = "/auth/refresh";

    public static final String USER_NEW = "/user/new";
    public static final String USER_EDIT = "/user/edit";
    public static final String USER_DELETE = "/user/delete/{id}";
    public static final String USERS = "/user/";
    public static final String USER = "/user/{id_or_username}";
    public static final String USER_ROLE_ADD = "/user/{username}/role/{roleName}/add";
    public static final String USER_ROLE_REMOVE = "/user/{username}/role/{roleName}/remove";

    public static final String ROLE_NEW = "/role/new";
    public static final String ROLE_EDIT = "/role/edit";
    public static final String ROLE_DELETE = "/role/delete/{id}";
    public static final String ROLES = "/role/";

}
