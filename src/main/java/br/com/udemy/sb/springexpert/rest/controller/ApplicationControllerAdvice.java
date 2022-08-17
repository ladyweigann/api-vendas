package br.com.udemy.sb.springexpert.rest.controller;

import br.com.udemy.sb.springexpert.exception.PedidoNaoEncontradoException;
import br.com.udemy.sb.springexpert.exception.RegraNegocioException;
import br.com.udemy.sb.springexpert.rest.ApiErrors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraDeNegocioException(RegraNegocioException ex) {
        String message = ex.getMessage();
        return new ApiErrors(message);

    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handlePedidoNotFoundException(PedidoNaoEncontradoException ex) {
        return new ApiErrors(ex.getMessage());

    }

}
