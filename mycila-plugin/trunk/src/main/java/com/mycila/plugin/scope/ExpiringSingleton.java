package com.mycila.plugin.scope;

/**
 * Same as {@link com.mycila.plugin.scope.Singleton}, except that the result is only valid for N milliseconds.
 * The time is specified by the parameter 'duration', in milliseconds.
 * When the export is requested and the duration expired, the method will be called to refresh the instance.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExpiringSingleton<T> extends ProviderSkeleton<T> {

    private long duration;

    private volatile T instance;
    private volatile long expire;

    @Override
    public void init(ScopeContext<T> context) {
        super.init(context);
        this.duration = Long.parseLong(context.getParameter("duration"));
    }

    @Override
    public T get() {
        if (expire < System.currentTimeMillis()) {
            synchronized (this) {
                if (expire < System.currentTimeMillis()) {
                    expire = System.currentTimeMillis() + duration;
                    instance = context.invoke();
                }
            }
        }
        return instance;
    }
}
