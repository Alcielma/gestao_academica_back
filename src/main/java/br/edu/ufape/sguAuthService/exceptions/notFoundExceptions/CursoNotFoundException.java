package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;


import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Curso não encontrado")
public class CursoNotFoundException extends ChangeSetPersister.NotFoundException {
}
