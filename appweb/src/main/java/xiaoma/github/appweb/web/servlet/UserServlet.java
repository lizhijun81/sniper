package xiaoma.github.appweb.web.servlet;

import xiaoma.github.appweb.web.annotatioin.Servlet;

/**
 * Created by Dell on 2015/5/5.
 */
@Servlet(value = "/user")
public class UserServlet {

    @Servlet(value = "/index")
    public String index() {
        return "/index";
    }
}
