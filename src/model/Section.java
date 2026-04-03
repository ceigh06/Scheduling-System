package model;

public class Section {
    private int sectionKey;
    private String sectionID;
    private String programCode;

    public Section(int sectionKey, String sectionID, String programCode) {
        this.sectionKey = sectionKey;
        this.sectionID = sectionID;
        this.programCode = programCode;
    }

    public int getSectionKey() {
        return sectionKey;
    }

    public String getSectionID() {
        return sectionID;
    }

    public String getProgramCode() {
        return programCode;
    }

    @Override
    public String toString() {
        return programCode + " - " + sectionID;
    }

}
