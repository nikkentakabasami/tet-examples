package examples;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/time/")
public class TimeServlet extends HttpServlet
{
    private static final TimeZone TZ = TimeZone.getDefault();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Locale locale = req.getLocale();
        Calendar cal = Calendar.getInstance(TZ,locale);
        String dateStr = DateFormat.getDateInstance(DateFormat.DEFAULT,locale).format(cal.getTime());
        String timeStr = DateFormat.getTimeInstance(DateFormat.DEFAULT,locale).format(cal.getTime());
        String tzStr = TZ.getDisplayName(false,TimeZone.SHORT,locale);
        resp.getWriter().println(String.format("%s %s %s",dateStr,timeStr,tzStr));
    }
}
