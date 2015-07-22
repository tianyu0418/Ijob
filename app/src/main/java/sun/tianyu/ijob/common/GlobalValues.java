package sun.tianyu.ijob.common;

import java.util.HashMap;

/**
 * Created by Developer on 15/07/22.
 */
public class GlobalValues {
    public HashMap<String, String> drawerTitle;

    public void clearAllData() {
        if (drawerTitle != null) {
            drawerTitle.clear();
            drawerTitle = null;
        }
    }

}
