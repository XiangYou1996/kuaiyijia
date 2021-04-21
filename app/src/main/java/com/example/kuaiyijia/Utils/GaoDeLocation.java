package com.example.kuaiyijia.Utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.kuaiyijia.Entity.Location;

/*
Author by: xy
Coding On 2021/3/11;
*/
public class GaoDeLocation {
    private final static String TAG = "GaoDeLocation";
    // 一个位置
    private Location location;
    //  声明一个地图定位客服端
    public AMapLocationClient mapLocationClient = null;
    //  回调定位监听器
    public AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            // 获取定位结果
            if (aMapLocation != null){
                if (aMapLocation.getErrorCode() == 0){
                    // 解析amaplocaiton
                    Log.i(TAG, "onLocationChanged: "+aMapLocation.getAddress());
                    location = new Location(aMapLocation.getLatitude(),aMapLocation.getLongitude(),aMapLocation.getAddress(),
                            aMapLocation.getCountry(),aMapLocation.getProvince(),aMapLocation.getCity(),aMapLocation.getStreet(),
                            aMapLocation.getStreetNum(),aMapLocation.getCityCode(),aMapLocation.getAdCode());
                    Log.i(TAG, "onLocationChanged: "+ location.getAddress());
                    mapLocationClient.stopLocation();
                }else {
                    Log.e("MapError", "error code "+ aMapLocation.getErrorCode()+", error info:" +aMapLocation.getErrorInfo() );
                }
            }
        }
    };
    // maplocationclientoption对象
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    //  各种初始化
    public void startClient(Context context){
        mapLocationClient = new AMapLocationClient(context);
        mapLocationClient.setLocationListener(mapLocationListener);
        // 设置定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setMockEnable(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        /*设置定位场景*/
//        mLocationOption.setLocationPurpose();  默认无场景
        // 给客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();
    }
    //
//    返回位置对象
    public Location getLocation(){
        Log.i(TAG, "getLocation: "+ location.getAddress());
        return location;
    }
    // 销毁定位服务
    public void closeLocation(){
        mapLocationClient.onDestroy();
    }
}
