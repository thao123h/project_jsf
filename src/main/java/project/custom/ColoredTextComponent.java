package project.custom;



import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@FacesComponent("custom.ColoredText")
public class ColoredTextComponent extends UIComponentBase {

    @Override
    public String getFamily() {
        return "custom";
    }

    public String getColor() {
        return (String) getStateHelper().eval("color", "black");
    }

    public void setColor(String color) {
        getStateHelper().put("color", color);
    }

    public String getText() {
        return (String) getStateHelper().eval("text", "");
    }

    public void setText(String text) {
        getStateHelper().put("text", text);
    }
}
