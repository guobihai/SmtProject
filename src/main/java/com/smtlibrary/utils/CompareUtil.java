package com.smtlibrary.utils;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by gbh on 16/12/30.
 */

public class CompareUtil {
    //sort 1正序 -1 倒序  filed 多字段排序
    public static <T> Comparator createComparator(int sort, String... filed) {
        return new ImComparator(sort, filed);
    }

    public static class ImComparator implements Comparator {
        int sort = 1;
        String[] filed;

        public ImComparator(int sort, String... filed) {
            this.sort = sort == -1 ? -1 : 1;
            this.filed = filed;
        }

        @Override
        public int compare(Object o1, Object o2) {
            int result = 0;
            for (String file : filed) {
                Object value1 = ReflexUtil.invokeMethod(file, o1);
                Object value2 = ReflexUtil.invokeMethod(file, o2);
                if (value1 == null || value2 == null) {
                    continue;
                }
                if (value1 instanceof Integer) { //Integer整数序排序
                    int v1 = Integer.valueOf(value1.toString());
                    int v2 = Integer.valueOf(value2.toString());
                    if (v1 == v2) continue;
                    if (sort == 1) {
                        return v1 - v2;
                    } else if (sort == -1) {
                        return v2 - v1;
                    }
                } else if (value1 instanceof Date) {  //Date类型排序
                    Date d1 = (Date) value1;
                    Date d2 = (Date) value2;
                    if (d1.compareTo(d2) == 0) continue;
                    if (sort == 1) {
                        return d1.compareTo(d2);
                    } else if (sort == -1) {
                        return d2.compareTo(d1);
                    }
                } else if (value1 instanceof Long) { //Long排序
                    long v1 = Long.valueOf(value1.toString());
                    long v2 = Long.valueOf(value2.toString());
                    if (v1 == v2) continue;
                    if (sort == 1) {
                        return v1 > v2 ? 1 : -1;
                    } else if (sort == -1) {
                        return v2 > v1 ? 1 : -1;
                    }
                } else if (value1 instanceof Double) { //Double排序
                    Double v1 = Double.valueOf(value1.toString());
                    Double v2 = Double.valueOf(value2.toString());
                    if (v1 == v2) continue;
                    if (sort == 1) {
                        return v1 > v2 ? 1 : -1;
                    } else if (sort == -1) {
                        return v2 > v1 ? 1 : -1;
                    }
                }

            }

            return result;
        }
    }
}
