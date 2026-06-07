package com.hortifruti.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(Exception ex, HttpServletRequest request) {
        return paginaErro(404, "Página não encontrada",
                "O endereço solicitado não existe ou foi movido.", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return paginaErro(403, "Acesso negado",
                "Você não tem permissão para acessar este recurso.", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleRegraNegocio(IllegalArgumentException ex, HttpServletRequest request) {
        return paginaErro(400, "Operação não permitida", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleErroInterno(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado em {}", request.getRequestURI(), ex);
        return paginaErro(500, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", request);
    }

    private ModelAndView paginaErro(int codigo, String titulo, String mensagem, HttpServletRequest request) {
        ModelAndView model = new ModelAndView("error/erro");
        model.addObject("codigo", codigo);
        model.addObject("titulo", titulo);
        model.addObject("mensagem", mensagem);
        model.addObject("caminho", request.getRequestURI());
        return model;
    }
}
