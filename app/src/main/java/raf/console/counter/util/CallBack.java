package raf.console.counter.util;

import raf.console.counter.util.CallbackInterface;public class CallBack {
    public static void addCallback(CallbackInterface callback){
        CallbackInterface.callbacks.add(callback);
    }
    public static void runAllCallbacks(){
        for(CallbackInterface c : CallbackInterface.callbacks){
            c.call();
        }
    }
}