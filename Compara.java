import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compara
{
    public Compara()
    {
        
    }
      public boolean licensa() throws ParseException 
      {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
          //Date date1 = sdf.parse("2009-12-31");
          Date date1 = sdf.parse("07-06-2022");//data em que expira
          Date date2 = new Date();
          sdf.format(date1);
          sdf.format(date2);
          if (date1.equals(date2)) 
          {
              
          }
          if (date1.after(date2)) 
          {
              return true;
          }
    
          if (date1.before(date2)) 
          {
              return false;
          }
        return false;
      }
}