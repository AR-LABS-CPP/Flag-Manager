package com.arlabs.myfm.states;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author AR-LABS
 */
public class FlagManagerStates {
    public static String WEBHOOK_ID = "";
    public static String FLAGS = "";
    public static Map<Integer, List<Object>> FLAGS_LIST = new HashMap<>();
    public static Map<Integer, List<Object>> WEBHOOKS_LIST = new HashMap<>();
    public static Map<Object, List<Object>> BINDINGS_LIST = new HashMap<>();
    public static Map<Integer, Integer> INVERSE_BINDINGS = new HashMap<>();
}
