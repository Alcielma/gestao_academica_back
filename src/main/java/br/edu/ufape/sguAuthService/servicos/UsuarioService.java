package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.UsuarioRepository;


import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;

import br.edu.ufape.sguAuthService.models.Visitante;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class UsuarioService implements br.edu.ufape.sguAuthService.servicos.interfaces.UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

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

    @Override
    public List<Usuario> buscarUsuariosPorKcId(List<String> kcIds) {
        List<Usuario> usuarios = usuarioRepository.findByKcIdIn(kcIds);

        Map<String, Usuario> usuarioMap = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getKcId, Function.identity()));

        List<Usuario> users =  kcIds.stream()
                .map(usuarioMap::get)
                .collect(Collectors.toList());

        logger.info("Usuarios encontrados: {}", users.getFirst().getAluno());
        return users;
    }



}
