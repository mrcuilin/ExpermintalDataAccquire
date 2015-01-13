/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author cuilin
 */
public class CAbstractController implements Controller {
    public HttpServletRequest req;
    public HttpServletResponse res;
    
    public String action;

    public String lang;

    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        this.req = hsr;
        this.res = hsr1;
        this.action = req.getParameter("action");
        return null;
    }
    
}
