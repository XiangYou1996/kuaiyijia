package com.example.kuaiyijia.Database;

/*
Author by: xy
Coding On 2021/4/16;
*/
public class DataBaseConfig {
    /*  连接配置*/
     //  config 1
    /*    private static String user = "sa";
    private static String password = "root123";
    private static String DatabaseName = "CQU";
    private static String IP = "172.20.53.32";*/
    // config 2
    public static String user = "app";
    public static String password = "app123456";
    public static String DatabaseName = "HYBAPP";
    public static String IP = "121.41.7.176";

    /*  具体配置  */
    // 线路相关信息
    public static String routeTableName = "PUB_LINE";
    public static String routePriID = "L_ID";
    public static String routeStartStation = "L_START";
    public static String routeEndStation = "L_END";
    public static String routeCID = "C_ID";
    public static String routeHYBID = "HYB_ID";
    // 班次 相关信息
    public static String BanciTableName = "PUB_CLASSES";
    public static String BanciPriID = "B_ID";
    public static String BanciLineID = "L_ID";
    public static String BanciCID = "C_ID";
    public static String BanciHYBID = "HYBID";
    public static String BanciSTIME = "B_STIME";

    /* 订单相关信息*/
    public static String OrderTableName = "orders";
    public static String OrderPriID = "id";
    public static String OrderNumber = "order_number";
    // 车辆相关信息
    /*网点、货运部相关信息*/
    public static String WebBranchTableName = "PUB_HYB";
    public static String WebBranchID = "HYBID";
    public static String WebBranchName = "HYBNAME";
    public static String WebBranchJC = "WDJC";
    public static String HuoYunTel = "HYBTEL";
    public static String WebBranchCantact = "HYBLXR";
    public static String WebBranchCantactTel = "HYBLXDH";
    public static String WebBranchDetailAddress = "HYBADDR";
    public static String WebBranchCID = "C_ID";

    /*用户相关信息*/
    public static String UserTableName = "Users";
    public static String UserAccount = "account";
    public static String UserPassword = "password";
    public static String UserPriID = "ID";
    public static String UserHYBID = "HYB_ID";
    public static String UserCID = "C_ID";

}
