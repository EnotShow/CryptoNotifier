package utils;

public class ListItem {
    private String text;
    private boolean switchState;

    public ListItem(String text, boolean switchState) {
        this.text = text;
        this.switchState = switchState;
    }

    public String getText() {
        return text;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

}
