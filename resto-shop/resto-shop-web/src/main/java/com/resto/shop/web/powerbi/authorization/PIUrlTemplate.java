package com.resto.shop.web.powerbi.authorization;

public class PIUrlTemplate {

    private PIUrlTemplate() {
    }

    public static final String API_URL = "https://api.powerbi.com";
    public static final String VERSION_1 = "v1.0";
    public static final String VERSION_BETA = "beta";
    public static final String CURRENT_VERSION = VERSION_1;
    public static final String BASE_URL = API_URL + "/" + CURRENT_VERSION + "/myorg/";
    public static final String GROUPS = BASE_URL + "groups/";
    public static final String DATASETS = BASE_URL + "datasets/";
    public static final String GROUP_DATASETS = GROUPS + "%s/datasets/";
    public static final String DATASET_DATASOURCES = DATASETS + "datasources/";
    public static final String GROUP_DATASET_DATASOURCES = GROUP_DATASETS + "%s/datasources/";
    public static final String DATASET_TABLES = DATASETS + "tables/";
    public static final String GROUP_DATASET_TABLES = GROUP_DATASETS + "%s/tables/";
    public static final String REPORTS = BASE_URL + "reports/";
    public static final String GROUP_REPORTS = GROUPS + "%s/reports/";
    public static final String DASHBOARDS = BASE_URL + "dashboards/";
    public static final String GROUP_DASHBOARDS = GROUPS + "%s/dashboards/";
    public static final String DASHBOARD_TILES = DASHBOARDS + "tiles/";
    public static final String GROUP_DASHBOARD_TILES = GROUP_DASHBOARDS + "%s/tiles/";
    public static final String GROUP_REPORTS_VIEW_TOKEN = GROUP_REPORTS + "%s/GenerateToken/";

    public static final String AUTHORITY = "https://login.microsoftonline.com";
    public static final String ACCESS_TOKEN_URL = AUTHORITY + "/%s/oauth2/token";
    public static final String GRANT_TYPE = "grant_type";
    public static final String RESOURCE = "resource";
    public static final String CLIENT_ID = "client_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String VIEW_TOKEN = "accessLevel";

    public static String username = "miao.yinjun@restoplus-tech.cn";
    public static String password = "Restoplus@vino18";
    public static String client_id = "7b0aaeec-59fb-4224-aea5-feb19b23e308";
    public static String tenant = "common";
    public static String resourceURI = "https://analysis.windows.net/powerbi/api";

}
