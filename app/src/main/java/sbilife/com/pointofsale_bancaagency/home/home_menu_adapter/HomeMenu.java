package sbilife.com.pointofsale_bancaagency.home.home_menu_adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Objects;

public class HomeMenu implements Serializable {
    private final int iconDrawable;
    private final String strMenuTitle;
    private final Class aClass;
    private final boolean homeTag;
    private final boolean driveLink;
    private final String link;
    private Bundle mBundle;

    public HomeMenu(int iconDrawable, String strMenuTitle, Class aClass, boolean homeTag,
                    boolean driveLink, String link, Bundle mBundle) {
        this.strMenuTitle = strMenuTitle;
        this.iconDrawable = iconDrawable;
        this.aClass = aClass;
        this.homeTag = homeTag;
        this.driveLink = driveLink;
        this.link = link;
        this.mBundle = mBundle;
    }

    Class getaClass() {
        return aClass;
    }

    boolean isHomeTag() {
        return homeTag;
    }

    boolean isDriveLink() {
        return driveLink;
    }

    String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeMenu homeMenu = (HomeMenu) o;
        return iconDrawable == homeMenu.iconDrawable &&
                strMenuTitle.equals(homeMenu.strMenuTitle);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(iconDrawable, strMenuTitle);
    }

    int getIconDrawable() {
        return iconDrawable;
    }


    String getStrMenuTitle() {
        return strMenuTitle;
    }

    public Bundle getmBundle() {
        return mBundle;
    }

    public void setmBundle(Bundle mBundle) {
        this.mBundle = mBundle;
    }
}
