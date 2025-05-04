package project.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import javax.faces.application.FacesMessage;
@FacesConverter("dateConverter")

public class DateConverter implements Converter<LocalDate> {

	private static final DateTimeFormatter[] SUPPORTED_FORMATS = new DateTimeFormatter[] {
		    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
		    DateTimeFormatter.ofPattern("MM-dd-yyyy"),
		    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
		    DateTimeFormatter.ofPattern("yyyy-MM-dd")
		};

	@Override
	public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
	    if (value == null || value.trim().isEmpty()) return null;
         
	    for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
	        try {
	        	 value = value.trim().replaceAll("\\b(\\d)\\b", "0$1");
	            return LocalDate.parse(value, formatter); 
	        } catch (DateTimeParseException e) {        	  
	            
	        }
	    }
    if (component instanceof EditableValueHolder) {
	        ((EditableValueHolder) component).setSubmittedValue(null);
	    }	    
	    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
   	            "Invalid date", null));
	}

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
    	return value != null ?  SUPPORTED_FORMATS[0].format(value) : "";
    }
}
