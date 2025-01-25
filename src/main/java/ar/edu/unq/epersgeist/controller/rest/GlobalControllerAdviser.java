package ar.edu.unq.epersgeist.controller.rest;

import ar.edu.unq.epersgeist.controller.dto.ErrorDTO;
import ar.edu.unq.epersgeist.exception.accionInvalida.AccionInvalidaException;
import ar.edu.unq.epersgeist.exception.notFound.NoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.NoHaySantuarioCorruptoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalControllerAdviser{

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NoEncontradoException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(NoHaySantuarioCorruptoException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ErrorDTO> handleSantuarioCorruptoException(NoEncontradoException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleDiferenteTipo(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("El valor '%s' no es válido para el parámetro '%s'.",
                ex.getValue(), ex.getName());
        ErrorDTO errorDTO = new ErrorDTO(errorMessage, HttpStatus.BAD_REQUEST.value());
        return  ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(AccionInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleAccionInvalidaException(AccionInvalidaException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}