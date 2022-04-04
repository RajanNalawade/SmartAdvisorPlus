package sbilife.com.pointofsale_bancaagency.reports.commonreports;

public class ServicingListValuesModel {
    private final String strMenuTitle;
    private final Class aClass;
    private final boolean homeTag;
    private final boolean isMtag;

    public ServicingListValuesModel(String strMenuTitle, Class aClass, boolean homeTag,boolean isMtag) {
        this.strMenuTitle = strMenuTitle;
        this.aClass = aClass;
        this.homeTag = homeTag;
        this.isMtag = isMtag;
    }

    public String getStrMenuTitle() {
        return strMenuTitle;
    }

    public Class getaClass() {
        return aClass;
    }

    public boolean isHomeTag() {
        return homeTag;
    }

    public boolean isMtag() {
        return isMtag;
    }
}
