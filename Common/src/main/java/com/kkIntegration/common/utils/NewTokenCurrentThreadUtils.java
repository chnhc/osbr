package com.kkIntegration.common.utils;

/**
 * description:
 * author: ckk
 * create: 2020-01-15 10:26
 */
public class NewTokenCurrentThreadUtils {

    private static final ThreadLocal<NewToken> CONTEXT_HOLDER = new ThreadLocal<>();


    public static void setNewToken(NewToken newToken){
        CONTEXT_HOLDER.set(newToken);
    }

    public static void setNewToken(String newToken){
        CONTEXT_HOLDER.set(new NewToken(newToken));
    }

    public static NewToken getNewToken(boolean isClear){
        NewToken n = CONTEXT_HOLDER.get();
        if(isClear){
            CONTEXT_HOLDER.remove();
        }
        return n;
    }

    public static NewToken getNewToken(){
       return CONTEXT_HOLDER.get();
    }

    public static class NewToken{

        boolean isRefresh = true;

        String newToken;

        public NewToken() {

        }

        public NewToken(String newToken) {
            this.newToken = newToken;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setRefresh(boolean refresh) {
            isRefresh = refresh;
        }

        public String getNewToken() {
            return newToken;
        }

        public void setNewToken(String newToken) {
            this.newToken = newToken;
        }
    }
}
