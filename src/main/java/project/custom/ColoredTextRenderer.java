
package project.custom;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;

import java.io.IOException;

public class ColoredTextRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ColoredTextComponent comp = (ColoredTextComponent) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", comp);
        writer.writeAttribute("style", "color:" + comp.getColor(), null);
        writer.write(comp.getText());
        writer.endElement("span");
    }
}
