package com.lchli.studydiscuss.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetConnectionUtils {


    private static ConnectionType getConnectionType(int networkType) {
        if (networkType == ConnectivityManager.TYPE_MOBILE_DUN || networkType == ConnectivityManager.TYPE_MOBILE)
            return ConnectionType.MOBILE;
        else if (networkType == ConnectivityManager.TYPE_ETHERNET) return ConnectionType.ETHERNET;
        else if (networkType == ConnectivityManager.TYPE_VPN) return ConnectionType.VPN;
        else if (networkType == ConnectivityManager.TYPE_WIFI) return ConnectionType.WIFI;
        else if (networkType == ConnectivityManager.TYPE_WIMAX) return ConnectionType.WIMAX;
        else return ConnectionType.UNKNOWN;
    }

    public static ConnectionType getActiveConnectionType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextProvider.context().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return getConnectionType(networkInfo.getType());
        } else return ConnectionType.UNKNOWN;
    }

    public static boolean isConnected() {
        return getActiveConnectionType() != ConnectionType.UNKNOWN;
    }


    public enum ConnectionType {
        ETHERNET,
        MOBILE,
        WIFI,
        WIMAX,
        VPN,
        UNKNOWN
    }

}