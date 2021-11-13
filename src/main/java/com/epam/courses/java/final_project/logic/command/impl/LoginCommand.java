package com.epam.courses.java.final_project.logic.command.impl;

import com.epam.courses.java.final_project.dao.impl.jdbc.JDBCException;
import com.epam.courses.java.final_project.logic.command.Command;
import com.epam.courses.java.final_project.logic.command.Response;
import com.epam.courses.java.final_project.model.User;
import com.epam.courses.java.final_project.service.UserService;
import com.epam.courses.java.final_project.util.PasswordCryptoPbkdf2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import static com.epam.courses.java.final_project.util.Constant.*;
import static com.epam.courses.java.final_project.util.CommandConstant.*;
import static com.epam.courses.java.final_project.util.Constant.LOG_TRACE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger(LOG_TRACE);

    @Override
    public Response execute(HttpServletRequest req, HttpServletResponse resp) throws JDBCException {
        String login = req.getParameter(PARAM_LOGIN);
        String password = req.getParameter(PARAM_PWD);
        log.trace("login of: " + login);

        Optional<User> user = UserService.findByLogin(login);

        if (user.isEmpty()){  // todo collapse after debugging
            req.getSession().setAttribute(ATTRIBUTE_LOGIN_ERROR, "User does not exist");
            return new Response(Response.Direction.Redirect, LOGIN_JSP);
        } else if (!PasswordCryptoPbkdf2.validatePwd(password, user.get().getPassword())){
            req.getSession().setAttribute(ATTRIBUTE_LOGIN_ERROR, "Incorrect password");
            return new Response(Response.Direction.Redirect, LOGIN_JSP);
        }

        req.getSession().setAttribute(ATTRIBUTE_LOGIN, user.get().getLogin());
        req.getSession().setAttribute(ATTRIBUTE_ROLE, user.get().getRole().name());
        return new Response(Response.Direction.Redirect, INDEX_JSP);
    }

    @Override
    public String getCommand() {
        return LOGIN;
    }
}
