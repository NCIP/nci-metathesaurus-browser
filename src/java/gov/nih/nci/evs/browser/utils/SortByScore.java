package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;

public class SortByScore {
    public enum Type { FALSE, TRUE, ALL };
    private Type type = Type.TRUE;
    private boolean sort_by_pt_only = true;
    private boolean apply_sort_score = true;

    public SortByScore() {
    }
    
    public SortByScore(SortByScore.Type type) {
        setType(type);
    }
    
    public boolean isSortByPtOnly() {
        return sort_by_pt_only;
    }
    
    public boolean isApplySortScore() {
        return apply_sort_score;
    }
    
    public Type getType()
    {
        return type;
    }
    
    public void setType(SortByScore.Type type) {
        this.type = type;
        switch (type) {
        case FALSE:
            sort_by_pt_only = true;
            apply_sort_score = false;
            break;
        case ALL:
            sort_by_pt_only = false;
            apply_sort_score = true;
            break;
        default: //TRUE 
            sort_by_pt_only = true;
            apply_sort_score = true;
            break;
        }
    }
    
    public void setType(String value) {
        if (value == null)
            return;  // Use default values
        setType(Type.valueOf(value.toUpperCase()));
    }
    
    public void setTypeByPropertyFile() {
        try {
            String value = NCImBrowserProperties.getProperty(
                NCImBrowserProperties.SORT_BY_SCORE);
            setType(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String toString() {
        return type.name().toLowerCase() + " (" +
            "sort_by_pt_only: " + sort_by_pt_only +
            ", apply_sort_score: " + apply_sort_score + ")";
    }
}

