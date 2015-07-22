package sun.tianyu.ijob;

import android.app.Application;
import android.util.Log;

import sun.tianyu.ijob.common.DefautValues;
import sun.tianyu.ijob.common.GlobalValues;

/**
 * Created by Developer on 15/07/22.
 */
public class IjobApplication extends Application{
    public  GlobalValues globalValues;
    public  DefautValues defautValues;// static 定数、初期化不要

    @Override
    public void onCreate() {
        super.onCreate();
        // グローバル値保存用のオブジェクト初期化
        globalValues = new GlobalValues();
    }

}
