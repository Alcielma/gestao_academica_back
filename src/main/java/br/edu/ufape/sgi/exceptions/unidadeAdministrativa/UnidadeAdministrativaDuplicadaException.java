package br.edu.ufape.sgi.exceptions.unidadeAdministrativa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Unidade Administrativa já existe!")
public class UnidadeAdministrativaDuplicadaException extends RuntimeException {
    public UnidadeAdministrativaDuplicadaException(String message) {
        super(message);
    }
}
