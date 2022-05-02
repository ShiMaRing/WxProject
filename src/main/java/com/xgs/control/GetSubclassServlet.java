package com.xgs.control;

import com.xgs.dao.dataDao.SubclassDao;
import com.xgs.pojo.Subclass;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author cjh
 * @date 2022/4/26 - 21:58
 */
@WebServlet("SubclassServlet")
public class GetSubclassServlet extends HttpServlet {
    @Autowired
    SubclassDao subclassDao;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = (String)req.getParameter("pid");
        List<Subclass> subclassList = subclassDao.findByPid(pid);
        HttpSession session = req.getSession();
        session.setAttribute("subclassList",subclassList);

    }
}
