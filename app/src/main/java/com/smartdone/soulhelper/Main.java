package com.smartdone.soulhelper;

import android.util.Log;

import java.lang.annotation.Target;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {

    private static final String TAG = "SOUL-APP-HELPER";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals("cn.soulapp.android")) {

            //去除水印
            XposedHelpers.findAndHookMethod("cn.soulapp.android.utils.bh", lpparam.classLoader, "b", String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e(TAG, "b:setresult: " + param.args[0]);
                    return param.args[0];
                }
            });
            XposedHelpers.findAndHookMethod("cn.soulapp.android.utils.bh", lpparam.classLoader, "c", String.class,
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e(TAG, "c:setresult: " + param.args[0]);
                            return param.args[0];
                        }
                    }
            );
            XposedHelpers.findAndHookMethod("cn.soulapp.android.im.utils.bc", lpparam.classLoader, "a", "com.hyphenate.chat.EMMessage", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.e(TAG, "revoke->a(Message)");
                }
            });

            XposedHelpers.findAndHookMethod("cn.soulapp.android.im.utils.bc", lpparam.classLoader, "a", "com.hyphenate.chat.EMMessage", "java.util.List", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e(TAG, "阻止消息撤回");
                    return null;
                }
            });

//            XposedHelpers.findAndHookMethod("cn.soulapp.android.im.utils.bc", lpparam.classLoader, "a", "com.hyphenate.chat.EMMessage", "java.util.List", new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                    Log.e(TAG, "revoke->a(Message, List)");
//                    Log.e(TAG, "EMMessage: " + param.args[0].toString());
//                    Log.e(TAG, "List: " + param.args[1].toString());
//                    Class<?> EMMessage = param.args[0].getClass();
//                    Log.e(TAG, "clasName: " + EMMessage.getName());
//                    Method getBody = EMMessage.getDeclaredMethod("getBody");
//                    getBody.setAccessible(true);
//
//                    Object eMMessageBody = getBody.invoke(param.args[0]);
//                    if(eMMessageBody.getClass().getName().toString().contains("EMTextMessageBody")) {
//                        Class<?> EMTextMessageBody = eMMessageBody.getClass();
//                        Method getMessage = EMTextMessageBody.getDeclaredMethod("getMessage");
//                        Object msg = getMessage.invoke(eMMessageBody);
//                        Log.e(TAG, "msg: " + msg.toString());
//                    }
//
//                    Method getStringAttribute = EMMessage.getDeclaredMethod("getStringAttribute", String.class, String.class);
//                    getStringAttribute.setAccessible(true);
//                    Object sa = getStringAttribute.invoke(param.args[0], "Revokeflag", null);
//                    if(sa != null) {
//                        Log.e(TAG, "Revokeflag: " + sa.toString());
//                    } else {
//                        Log.e(TAG, "Revokeflag: null");
//                    }
//
//                }
//            });

        }
    }
}
