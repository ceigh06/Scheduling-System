package model;

public class Course {
    private String code;
    private String programCode;
    private String description;
    private int units;
    private boolean isMajor;
    private boolean isArchived;
    private int section;

    public void setIsArchived(Boolean isArchived){
        this.isArchived = isArchived;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUnits(int units) {
        this.units = units;
    }
    public void setMajor(boolean isMajor) {
        this.isMajor = isMajor;
    }
    public void setSection(int section) {
        this.section = section;
    }

    public int getSection(){
        return section;
    }
    public String getCode() {
        return code;
    }
    public String getProgramCode() {
        return programCode;
    }
    public String getDescription() {
        return description;
    }
    public int getUnits() {
        return units;
    }
    public boolean isMajor() {
        return isMajor;
    }

}
