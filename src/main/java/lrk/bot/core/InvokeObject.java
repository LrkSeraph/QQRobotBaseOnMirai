package lrk.bot.core;

import lrk.bot.core.listener.EventHandler;
import lrk.bot.main.RobotNotification;

import java.lang.reflect.Method;

class InvokeObject {
    private final Object object;
    private final Method method;
    private final int priority;
    private final boolean ignoreCancelled;

    public InvokeObject(Object object, Method method) {
        this.object = object;
        this.method = method;
        EventHandler eventHandler = method.getAnnotation(EventHandler.class);
        priority = eventHandler.priority();
        ignoreCancelled = eventHandler.ignoreCancelled();
    }

    public void invoke(Object... params) {
        try {
            method.setAccessible(true);
            method.invoke(object, params);
        } catch (Exception e) {
            RobotNotification.Warning(String.format("%s->%s: %s", getClass().getName(), e.getClass().getName(), e.getMessage()));
        }
    }

    public int getPriority() {
        return priority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

}
