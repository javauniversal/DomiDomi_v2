package com.appgestor.domidomi.utils;

import com.appgestor.domidomi.Entities.ConfigSplash;
import com.appgestor.domidomi.cnst.Flags;

/**
 * Created by varsovski on 27-Sep-15.
 */
public class ValidationUtil {

    public static int hasPath(ConfigSplash cs) {
        if (cs.getPathSplash().isEmpty())
            return Flags.WITH_LOGO;
        else
            return Flags.WITH_PATH;
    }
}
