package web.listener;

import org.springframework.context.ApplicationEvent;
import web.model.User;

import java.io.Serializable;
import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent implements Serializable {

    private String appUrl;
    private Locale locale;
    private User user;


    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl){
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
