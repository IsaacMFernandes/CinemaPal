package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import java.util.ArrayList;

public abstract class CallbackTask<T> {
    public interface OnSuccessListener<T> {
        void onSuccess(T result);
    }
    public interface OnFailureListener {
        void onFailure(Exception e);
    }
    public interface Join<T, U> {
        CallbackTask<U> join(T result);
    }
    public interface Map<T, U> {
        U map(T result) throws Exception;
    }

    private static class MapTask<T, U> extends CallbackTask<U> {
        protected CallbackTask<T> original;

        @Override
        public void execute() {
            original.execute();
        }
    }

    private static class JoinTask<T, U> extends CallbackTask<U> {
        protected CallbackTask<T> original;

        @Override
        public void execute() {
            original.execute();
        }
    }

    private final ArrayList<OnSuccessListener<T>> onSuccess = new ArrayList<>();
    protected OnFailureListener onFailure;

    protected final void callOnSuccess(T result) {
        for(OnSuccessListener<T> onSuccess : this.onSuccess) {
            onSuccess.onSuccess(result);
        }
    }

    public abstract void execute();

    public CallbackTask<T> setOnSuccessListener(OnSuccessListener<T> onSuccess) {
        this.onSuccess.add(onSuccess);
        return this;
    }
    public final <U> CallbackTask<U> map(Map<T, U> map) {
        MapTask<T, U> task = new MapTask<>();
        task.original = this;

        this.setOnSuccessListener(result -> {
            try {
                U val = map.map(result);
                task.callOnSuccess(val);
            }
            catch(Exception e) {
                task.onFailure.onFailure(e);
            }
        });
        task.setOnFailureListener(this.onFailure);
        this.onFailure = null;

        return task;
    }
    public final <U> CallbackTask<U> join(Join<T, U> join) {
        JoinTask<T, U> task = new JoinTask<>();
        task.original = this;

        this.setOnSuccessListener(result -> join.join(result)
                .setOnSuccessListener(task::callOnSuccess)
                .execute());
        task.setOnFailureListener(this.onFailure);
        this.onFailure = null;

        return task;
    }
    public CallbackTask<T> setOnFailureListener(OnFailureListener onFailure) {
        this.onFailure = onFailure;
        return this;
    }
}
