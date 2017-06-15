package cl.subtel.interop.cntv.util;
 
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
 
public class FileProperties
{
   public static String getParamProperties(String key, String nomProperties) throws IOException, MissingResourceException
   {
     ResourceBundle rb = ResourceBundle.getBundle(nomProperties);
     String pathTmp = rb.getString(key);
     return pathTmp;
  } 
}

