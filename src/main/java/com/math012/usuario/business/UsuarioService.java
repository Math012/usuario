package com.math012.usuario.business;

import com.math012.usuario.business.converter.UsuarioConverter;
import com.math012.usuario.business.dto.UsuarioDTO;
import com.math012.usuario.infrastructure.entity.Usuario;
import com.math012.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.usuarioDTOParaUsuario(usuarioDTO);
        return usuarioConverter.usuarioParaUsuarioDTO(usuarioRepository.save(usuario));
    }

}
