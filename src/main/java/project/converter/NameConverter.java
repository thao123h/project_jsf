package project.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
@FacesConverter("nameConverter")
public class NameConverter implements Converter<String>{

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || value.trim().isEmpty()) return null;
		   String[] words = value.trim().toLowerCase().split("\\s+");
	        StringBuilder result = new StringBuilder();

	        for (String word : words) {
	            if (!word.isEmpty()) {
	                result.append(Character.toUpperCase(word.charAt(0)))
	                      .append(word.substring(1))
	                      .append(" ");
	            }
	        }

	        return result.toString().trim(); 
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		return value == null ? "" : value.toString();
	}

}
