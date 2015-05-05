package xiaoma.github.appweb.web.servlet;

import xiaoma.github.appweb.web.annotatioin.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dell on 2015/5/5.
 */
@Servlet(value = "/user")
public class UserServlet {

    @Servlet(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "/index";
    }
}
