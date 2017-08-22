package org.lovebing.reactnative.baidumap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by lovebing on Sept 28, 2016.
 */
public class MarkerUtil {

    public static void updateMaker(Marker maker, ReadableMap option) {
        LatLng position = getLatLngFromOption(option);
        maker.setPosition(position);
        maker.setTitle(option.getString("title"));
    }
    private static String tag = "ReactNativeJS";
    public static Marker addMarker(Context context, MapView mapView, ReadableMap option) {

        String path = context.getFilesDir().getAbsolutePath()+"/"+option.getString("icon");
        Bitmap icon = BitmapFactory.decodeFile(path);
        Bitmap scaleIcon = scale(icon);
        Log.i(tag,"addMarker , fileName = "+path);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(radius(scaleIcon));
        if(bitmap == null){
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        }
        LatLng position = getLatLngFromOption(option);
        OverlayOptions overlayOptions = new MarkerOptions()
                .icon(bitmap)
                .position(position)
                .title(option.getString("title"));

        Marker marker = (Marker)mapView.getMap().addOverlay(overlayOptions);
        return marker;
    }

    private static LatLng getLatLngFromOption(ReadableMap option) {
        double latitude = option.getDouble("latitude");
        double longitude = option.getDouble("longitude");
        return new LatLng(latitude, longitude);

    }
    private static Bitmap radius(Bitmap bitmap){
        int width = 128,height=128,roundPx=64;
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    private static Bitmap scale(Bitmap bitmap){
        float width = 128f/bitmap.getWidth();
        float height = 128f/bitmap.getHeight();
        Log.i(tag,"scale : width = "+width+ " , height = "+height);
        Matrix matrix = new Matrix();
        matrix.postScale(width,height); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
}
