package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.UsuarioRepository;


import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;

import br.edu.ufape.sguAuthService.models.Visitante;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class UsuarioService implements br.edu.ufape.sguAuthService.servicos.interfaces.UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        Visitante visitante = new Visitante();
        usuario.adicionarPerfil(visitante);
        return usuarioRepository.save(usuario);
    }
    @Override
    public Usuario editarUsuario(String idSessao, Usuario novoUsuario) throws UsuarioNotFoundException {
        Usuario antigoUsuario =  usuarioRepository.findByKcId(idSessao).orElseThrow(UsuarioNotFoundException::new);
        modelMapper.map(novoUsuario, antigoUsuario);
        return usuarioRepository.save(antigoUsuario);
    }

    @Override
    public Usuario buscarUsuario(Long id, boolean isAdm, String sessionId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getKcId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        return usuario;
    }

    @Override
    public Usuario buscarUsuarioPorKcId(String kcId) throws UsuarioNotFoundException {
        return usuarioRepository.findByKcId(kcId).orElseThrow(UsuarioNotFoundException::new);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findByAtivoTrue();
    }

    @Override
    public void deletarUsuario(String sessionId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findByKcId(sessionId).orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void deletarUsuarioKcId(String kcId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findByKcId(kcId).orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

//    @Override
//    public Usuario atualizar(Usuario usuario) {
//        return usuarioRepository.save(usuario);
//    }


}
