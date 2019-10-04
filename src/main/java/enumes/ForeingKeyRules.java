package enumes;

public enum ForeingKeyRules {
    Cascade ("0"),
    KeyRestrict ("1"),
    KeySetNull ("2"),
    NoAction ("3"),
    SetDefault ("4");


    private String title;

    ForeingKeyRules(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
