package loyaltyscheme;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet")
public class JPAServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        write(resp, "Hello world! This is a test outprint!");

    }
    private void write(HttpServletResponse resp, String string) throws IOException{
        resp.getWriter().append(string);
        resp.getWriter().append("\n");
    }
}