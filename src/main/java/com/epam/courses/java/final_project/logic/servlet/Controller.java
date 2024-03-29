package com.epam.courses.java.final_project.logic.servlet;

import com.epam.courses.java.final_project.dao.impl.jdbc.JDBCException;
import com.epam.courses.java.final_project.logic.command.Command;
import com.epam.courses.java.final_project.logic.command.CommandContainer;
import com.epam.courses.java.final_project.logic.command.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.epam.courses.java.final_project.util.constant.Constant.LOG_INFO;
import static com.epam.courses.java.final_project.util.constant.Constant.LOG_TRACE;

/**
 * Main Servlet that call execute method of needed {@code Command}. (Command design pattern)
 *
 * @author Kostiantyn Kolchenko
 */
public class Controller extends HttpServlet {

    private static final Logger log = LogManager.getLogger(LOG_INFO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqName = req.getParameter("command");
        Command command = CommandContainer.getInstance().getCommandGet(
                Objects.requireNonNullElseGet(
                        reqName, () -> req.getServletPath().substring(1).replace(".act", "")
                )
        );
        log.info("get command => " + command.getCommand());

        try {
            responseHandler(command.execute(req, resp), req, resp);
        } catch (JDBCException e) {
            responseHandler(new Response(Response.Direction.Error,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()), req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqName = req.getParameter("command");
        String path = req.getServletPath();
        Command command = CommandContainer.getInstance().getCommandPost(
                Objects.requireNonNullElseGet(
                        reqName, () -> path.substring(1).replace(".act", "")
                )
        );
        log.info("post command => " + command.getCommand());

        try {
            responseHandler(command.execute(req, resp), req, resp);
        } catch (JDBCException e) {
            responseHandler(new Response(Response.Direction.Error,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()), req, resp);
        }
    }

    private void responseHandler(Response response, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info(response);
        if (response.getDirection().equals(Response.Direction.Forward)) {
            req.getRequestDispatcher(response.getResponse()).forward(req, resp);
        } else if (response.getDirection().equals(Response.Direction.Redirect)) {
            resp.sendRedirect(response.getResponse());
        } else {
            resp.sendError(response.getErrCode(), response.getResponse());
        }
    }
}
