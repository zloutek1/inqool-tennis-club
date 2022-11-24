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
    public static final String USER_RESERVATIONS = "/user/{id_or_username}/reservations";
    public static final String USER_ROLE_ADD = "/user/{username}/role/{roleName}/add";
    public static final String USER_ROLE_REMOVE = "/user/{username}/role/{roleName}/remove";

    public static final String ROLE_NEW = "/role/new";
    public static final String ROLE_EDIT = "/role/edit";
    public static final String ROLE_DELETE = "/role/delete/{id}";
    public static final String ROLES = "/role/";

    public static final String COURT_NEW = "/court/new";
    public static final String COURT_EDIT = "/court/edit";
    public static final String COURT_DELETE = "/court/delete/{id}";
    public static final String COURTS = "/court/";
    public static final String COURT_RESERVATIONS = "/court/{id}/reservations";

    public static final String RESERVATION_NEW = "/reservation/new";
    public static final String RESERVATION_EDIT = "/reservation/edit";
    public static final String RESERVATION_DELETE = "/reservation/delete/{id}";
    public static final String RESERVATIONS = "/reservation/";

    public static final String TERRAIN_NEW = "/terrain/new";
    public static final String TERRAIN_EDIT = "/terrain/edit";
    public static final String TERRAIN_DELETE = "/terrain/delete/{id}";
    public static final String TERRAINS = "/terrain/";

}
