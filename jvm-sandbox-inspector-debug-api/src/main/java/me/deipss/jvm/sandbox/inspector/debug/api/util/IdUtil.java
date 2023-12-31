package me.deipss.jvm.sandbox.inspector.debug.api.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IdUtil {
    public static String id(String prefix){
        return prefix+"_"+TimeUtil.formatToday()+"_"+ LocalTime.now().getMinute() + StringUtils.leftPad(LocalDateTime.now().getNano()+"",11,'0');
    }
}
